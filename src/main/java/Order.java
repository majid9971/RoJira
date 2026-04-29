import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a customer order containing one or more items.
 * Tracks the full lifecycle of the order including status history.
 */
public class Order {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final String orderId;
    private final String customerId;
    private final List<OrderItem> items;
    private OrderStatus status;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String shippingAddress;
    private final List<OrderStatusChange> statusHistory;

    /**
     * Creates a new Order.
     *
     * @param orderId         the unique order identifier
     * @param customerId      the customer who placed the order
     * @param shippingAddress the delivery address
     * @throws IllegalArgumentException if orderId or customerId is null/empty
     */
    public Order(String orderId, String customerId, String shippingAddress) {
        if (orderId == null || orderId.isEmpty()) {
            throw new IllegalArgumentException("Order ID cannot be null or empty");
        }
        if (customerId == null || customerId.isEmpty()) {
            throw new IllegalArgumentException("Customer ID cannot be null or empty");
        }
        this.orderId = orderId;
        this.customerId = customerId;
        this.shippingAddress = shippingAddress != null ? shippingAddress : "";
        this.items = new ArrayList<>();
        this.status = OrderStatus.PENDING;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
        this.statusHistory = new ArrayList<>();

        // Record initial status
        statusHistory.add(new OrderStatusChange(null, OrderStatus.PENDING, "Order created"));
    }

    public String getOrderId() {
        return orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Returns an unmodifiable view of the order items.
     *
     * @return the list of order items
     */
    public List<OrderItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    /**
     * Adds an item to the order. Only allowed when order is in PENDING status.
     *
     * @param item the item to add
     * @throws IllegalStateException if order is not in PENDING status
     */
    public void addItem(OrderItem item) {
        if (status != OrderStatus.PENDING) {
            throw new IllegalStateException("Cannot modify items when order is " + status);
        }
        items.add(item);
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Removes an item from the order by product ID. Only allowed when order is in PENDING status.
     *
     * @param productId the product ID to remove
     * @return true if the item was removed, false if not found
     * @throws IllegalStateException if order is not in PENDING status
     */
    public boolean removeItem(String productId) {
        if (status != OrderStatus.PENDING) {
            throw new IllegalStateException("Cannot modify items when order is " + status);
        }
        boolean removed = items.removeIf(item -> item.getProductId().equals(productId));
        if (removed) {
            this.updatedAt = LocalDateTime.now();
        }
        return removed;
    }

    /**
     * Calculates the total amount for the order.
     *
     * @return the sum of all item totals
     */
    public double getTotalAmount() {
        return items.stream()
                .mapToDouble(OrderItem::getTotalPrice)
                .sum();
    }

    /**
     * Transitions the order to a new status.
     *
     * @param newStatus the target status
     * @param notes     optional notes about the transition
     * @throws IllegalStateException if the transition is not valid
     */
    public void updateStatus(OrderStatus newStatus, String notes) {
        if (!status.canTransitionTo(newStatus)) {
            throw new IllegalStateException(
                    String.format("Cannot transition from %s to %s", status, newStatus));
        }
        OrderStatus oldStatus = this.status;
        this.status = newStatus;
        this.updatedAt = LocalDateTime.now();
        statusHistory.add(new OrderStatusChange(oldStatus, newStatus, notes));
    }

    /**
     * Returns an unmodifiable view of the order's status change history.
     *
     * @return the list of status changes
     */
    public List<OrderStatusChange> getStatusHistory() {
        return Collections.unmodifiableList(statusHistory);
    }

    /**
     * Returns a formatted summary of the order.
     *
     * @return a multi-line string representing the order
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("╔══════════════════════════════════════════════════════════════╗\n");
        sb.append(String.format("║ Order: %-54s║\n", orderId));
        sb.append("╠══════════════════════════════════════════════════════════════╣\n");
        sb.append(String.format("║ Customer:  %-50s║\n", customerId));
        sb.append(String.format("║ Status:    %-50s║\n", status + " - " + status.getDescription()));
        sb.append(String.format("║ Created:   %-50s║\n", createdAt.format(FORMATTER)));
        sb.append(String.format("║ Updated:   %-50s║\n", updatedAt.format(FORMATTER)));
        sb.append(String.format("║ Ship To:   %-50s║\n", shippingAddress));
        sb.append("╠══════════════════════════════════════════════════════════════╣\n");
        sb.append("║ Items:                                                       ║\n");
        if (items.isEmpty()) {
            sb.append("║   (no items)                                                 ║\n");
        } else {
            for (OrderItem item : items) {
                sb.append("║ ").append(item.toString()).append("  ║\n");
            }
        }
        sb.append("╠══════════════════════════════════════════════════════════════╣\n");
        sb.append(String.format("║ Total: $%-53.2f║\n", getTotalAmount()));
        sb.append("╚══════════════════════════════════════════════════════════════╝\n");
        return sb.toString();
    }
}
