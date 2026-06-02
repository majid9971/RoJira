import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Repository providing access to the product catalog.
 * Uses an in-memory store with sample data for demonstration.
 */
public class ProductRepository {

    private final List<Product> products;

    public ProductRepository() {
        this.products = new ArrayList<>();
        loadSampleData();
    }

    /**
     * Returns all products in the catalog.
     */
    public List<Product> findAll() {
        return Collections.unmodifiableList(products);
    }

    /**
     * Finds a product by its ID.
     */
    public Optional<Product> findById(String id) {
        return products.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }

    /**
     * Returns all distinct categories.
     */
    public List<String> findAllCategories() {
        List<String> categories = new ArrayList<>();
        for (Product p : products) {
            if (!categories.contains(p.getCategory())) {
                categories.add(p.getCategory());
            }
        }
        Collections.sort(categories);
        return categories;
    }

    private void loadSampleData() {
        products.add(new Product("P001", "Wireless Bluetooth Headphones",
                "Premium over-ear headphones with active noise cancellation and 30-hour battery life.",
                79.99, "Electronics",
                "https://placehold.co/300x200/4A90D9/FFFFFF?text=Headphones", 45, 4.5));

        products.add(new Product("P002", "Organic Green Tea (100 bags)",
                "Certified organic green tea sourced from Japanese highlands. Rich antioxidant content.",
                14.99, "Groceries",
                "https://placehold.co/300x200/6BBF59/FFFFFF?text=Green+Tea", 200, 4.7));

        products.add(new Product("P003", "Running Shoes - UltraBoost",
                "Lightweight performance running shoes with responsive cushioning and breathable mesh upper.",
                129.99, "Footwear",
                "https://placehold.co/300x200/E8734A/FFFFFF?text=Shoes", 30, 4.3));

        products.add(new Product("P004", "Stainless Steel Water Bottle",
                "Double-wall vacuum insulated 750ml bottle. Keeps drinks cold for 24h or hot for 12h.",
                24.99, "Home & Kitchen",
                "https://placehold.co/300x200/9B59B6/FFFFFF?text=Bottle", 150, 4.6));

        products.add(new Product("P005", "Mechanical Keyboard RGB",
                "Full-size mechanical keyboard with Cherry MX Blue switches and customizable RGB lighting.",
                89.99, "Electronics",
                "https://placehold.co/300x200/4A90D9/FFFFFF?text=Keyboard", 60, 4.4));

        products.add(new Product("P006", "Yoga Mat - Premium Non-Slip",
                "Extra thick 6mm eco-friendly TPE yoga mat with alignment lines and carrying strap.",
                34.99, "Sports & Fitness",
                "https://placehold.co/300x200/1ABC9C/FFFFFF?text=Yoga+Mat", 80, 4.8));

        products.add(new Product("P007", "LED Desk Lamp",
                "Adjustable LED desk lamp with 5 brightness levels, 3 color temperatures, and USB charging port.",
                42.99, "Home & Kitchen",
                "https://placehold.co/300x200/9B59B6/FFFFFF?text=Desk+Lamp", 95, 4.2));

        products.add(new Product("P008", "Leather Wallet - Slim Bifold",
                "Genuine leather slim bifold wallet with RFID blocking technology. Holds 8 cards.",
                29.99, "Accessories",
                "https://placehold.co/300x200/8B6914/FFFFFF?text=Wallet", 120, 4.1));

        products.add(new Product("P009", "Portable Bluetooth Speaker",
                "Waterproof portable speaker with 360-degree sound and 12-hour playtime.",
                49.99, "Electronics",
                "https://placehold.co/300x200/4A90D9/FFFFFF?text=Speaker", 75, 4.6));

        products.add(new Product("P010", "Organic Coffee Beans (1kg)",
                "Single-origin Arabica coffee beans from Colombia. Medium roast with chocolate notes.",
                18.99, "Groceries",
                "https://placehold.co/300x200/6BBF59/FFFFFF?text=Coffee", 180, 4.9));

        products.add(new Product("P011", "Backpack - Travel Pro",
                "40L expandable travel backpack with laptop compartment, anti-theft pocket, and rain cover.",
                64.99, "Accessories",
                "https://placehold.co/300x200/8B6914/FFFFFF?text=Backpack", 55, 4.5));

        products.add(new Product("P012", "Smart Fitness Tracker",
                "Advanced fitness tracker with heart rate monitoring, sleep tracking, and 7-day battery.",
                59.99, "Electronics",
                "https://placehold.co/300x200/4A90D9/FFFFFF?text=Tracker", 0, 4.3));

        products.add(new Product("P013", "Cast Iron Skillet 12-inch",
                "Pre-seasoned cast iron skillet suitable for all cooktops including induction. Oven safe to 500°F.",
                39.99, "Home & Kitchen",
                "https://placehold.co/300x200/9B59B6/FFFFFF?text=Skillet", 40, 4.7));

        products.add(new Product("P014", "Resistance Bands Set",
                "Set of 5 resistance bands with varying resistance levels. Includes door anchor and carry bag.",
                19.99, "Sports & Fitness",
                "https://placehold.co/300x200/1ABC9C/FFFFFF?text=Bands", 200, 4.4));

        products.add(new Product("P015", "Hiking Boots - Waterproof",
                "Mid-cut waterproof hiking boots with Vibram sole and ankle support. All-terrain grip.",
                149.99, "Footwear",
                "https://placehold.co/300x200/E8734A/FFFFFF?text=Boots", 25, 4.6));
    }
}
