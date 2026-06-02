import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service layer for product catalog operations.
 * Provides filtering, sorting, searching, and pagination.
 */
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    /**
     * Retrieves products with optional filtering, sorting, searching, and pagination.
     *
     * @param category  filter by category (null or empty for all)
     * @param search    search term for name/description (null or empty for all)
     * @param sortBy    sort field: "name", "price", "rating" (default: "name")
     * @param sortOrder sort order: "asc" or "desc" (default: "asc")
     * @param page      page number, 1-based (default: 1)
     * @param pageSize  items per page (default: 12)
     * @return a ProductPage containing the results and pagination info
     */
    public ProductPage getProducts(String category, String search, String sortBy,
                                   String sortOrder, int page, int pageSize) {
        List<Product> filtered = new ArrayList<>(repository.findAll());

        // Filter by category
        if (category != null && !category.isEmpty()) {
            filtered = filtered.stream()
                    .filter(p -> p.getCategory().equalsIgnoreCase(category))
                    .collect(Collectors.toList());
        }

        // Search by name or description
        if (search != null && !search.isEmpty()) {
            String searchLower = search.toLowerCase();
            filtered = filtered.stream()
                    .filter(p -> p.getName().toLowerCase().contains(searchLower)
                            || p.getDescription().toLowerCase().contains(searchLower))
                    .collect(Collectors.toList());
        }

        // Sort
        Comparator<Product> comparator = getComparator(sortBy);
        if ("desc".equalsIgnoreCase(sortOrder)) {
            comparator = comparator.reversed();
        }
        filtered.sort(comparator);

        // Pagination
        int totalItems = filtered.size();
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);
        page = Math.max(1, Math.min(page, Math.max(1, totalPages)));
        int fromIndex = (page - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, totalItems);

        List<Product> pageItems = (fromIndex < totalItems)
                ? filtered.subList(fromIndex, toIndex)
                : new ArrayList<>();

        return new ProductPage(pageItems, page, pageSize, totalItems, totalPages);
    }

    /**
     * Finds a single product by its ID.
     */
    public Optional<Product> getProductById(String id) {
        return repository.findById(id);
    }

    /**
     * Returns all available product categories.
     */
    public List<String> getCategories() {
        return repository.findAllCategories();
    }

    private Comparator<Product> getComparator(String sortBy) {
        if (sortBy == null) sortBy = "name";
        switch (sortBy.toLowerCase()) {
            case "price":
                return Comparator.comparingDouble(Product::getPrice);
            case "rating":
                return Comparator.comparingDouble(Product::getRating);
            case "name":
            default:
                return Comparator.comparing(Product::getName, String.CASE_INSENSITIVE_ORDER);
        }
    }

    /**
     * Holds a page of product results along with pagination metadata.
     */
    public static class ProductPage {
        private final List<Product> items;
        private final int page;
        private final int pageSize;
        private final int totalItems;
        private final int totalPages;

        public ProductPage(List<Product> items, int page, int pageSize, int totalItems, int totalPages) {
            this.items = items;
            this.page = page;
            this.pageSize = pageSize;
            this.totalItems = totalItems;
            this.totalPages = totalPages;
        }

        public List<Product> getItems() { return items; }
        public int getPage() { return page; }
        public int getPageSize() { return pageSize; }
        public int getTotalItems() { return totalItems; }
        public int getTotalPages() { return totalPages; }

        public String toJson() {
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            sb.append("\"page\":").append(page).append(",");
            sb.append("\"pageSize\":").append(pageSize).append(",");
            sb.append("\"totalItems\":").append(totalItems).append(",");
            sb.append("\"totalPages\":").append(totalPages).append(",");
            sb.append("\"items\":[");
            for (int i = 0; i < items.size(); i++) {
                if (i > 0) sb.append(",");
                sb.append(items.get(i).toJson());
            }
            sb.append("]}");
            return sb.toString();
        }
    }
}
