/**
 * Represents an individual item within an order.
 * Each order item tracks the product, quantity, and pricing information.
 */
public class OrderItem {
    private final String productId;
    private final String productName;
    private int quantity;
    private final double unitPrice;

    /**
     * Creates a new OrderItem.
     *
     * @param productId   the unique product identifier
     * @param productName the display name of the product
     * @param quantity    the quantity ordered (must be positive)
     * @param unitPrice   the price per unit (must be non-negative)
     * @throws IllegalArgumentException if quantity is not positive or unitPrice is negative
     */
    public OrderItem(String productId, String productName, int quantity, double unitPrice) {
        if (productId == null || productId.isEmpty()) {
            throw new IllegalArgumentException("Product ID cannot be null or empty");
        }
        if (productName == null || productName.isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be null or empty");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        if (unitPrice < 0) {
            throw new IllegalArgumentException("Unit price cannot be negative");
        }
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    /**
     * Updates the quantity of this order item.
     *
     * @param quantity the new quantity (must be positive)
     * @throws IllegalArgumentException if quantity is not positive
     */
    public void setQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    /**
     * Calculates the total price for this item (quantity * unit price).
     *
     * @return the total price for this line item
     */
    public double getTotalPrice() {
        return quantity * unitPrice;
    }

    @Override
    public String toString() {
        return String.format("  %-20s | Qty: %3d | Unit: $%8.2f | Total: $%8.2f",
                productName, quantity, unitPrice, getTotalPrice());
    }
}
