/**
 * Represents a product in the catalog.
 */
public class Product {

    private final String id;
    private final String name;
    private final String description;
    private final double price;
    private final String category;
    private final String imageUrl;
    private final int stockQuantity;
    private final double rating;

    public Product(String id, String name, String description, double price,
                   String category, String imageUrl, int stockQuantity, double rating) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.imageUrl = imageUrl;
        this.stockQuantity = stockQuantity;
        this.rating = rating;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public double getRating() {
        return rating;
    }

    public boolean isInStock() {
        return stockQuantity > 0;
    }

    /**
     * Serializes this product to a JSON string.
     */
    public String toJson() {
        return "{"
                + "\"id\":\"" + escapeJson(id) + "\","
                + "\"name\":\"" + escapeJson(name) + "\","
                + "\"description\":\"" + escapeJson(description) + "\","
                + "\"price\":" + price + ","
                + "\"category\":\"" + escapeJson(category) + "\","
                + "\"imageUrl\":\"" + escapeJson(imageUrl) + "\","
                + "\"stockQuantity\":" + stockQuantity + ","
                + "\"rating\":" + rating + ","
                + "\"inStock\":" + isInStock()
                + "}";
    }

    private static String escapeJson(String value) {
        if (value == null) return "";
        return value.replace("\\", "\\\\")
                     .replace("\"", "\\\"")
                     .replace("\n", "\\n")
                     .replace("\r", "\\r")
                     .replace("\t", "\\t");
    }
}
