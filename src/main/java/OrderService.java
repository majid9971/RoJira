import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Service class that manages order operations including creation, tracking,
 * status management, and order history retrieval.
 *
 * Uses in-memory storage for orders. In a production system, this would
 * be backed by a database.
 */
public class OrderService {
    private final Map<String, Order> orders;
    private final AtomicLong orderCounter;
    private final String orderPrefix;

    /**
     * Creates a new OrderService with default order ID prefix "ORD".
     */
    public OrderService() {
        this("ORD");
    }

    /**
     * Creates a new OrderService with a custom order ID prefix.
     *
     * @param orderPrefix the prefix for generated order IDs
     */
    public OrderService(String orderPrefix) {
        this.orders = new HashMap<>();
        this.orderCounter = new AtomicLong(1000);
        this.orderPrefix = orderPrefix;
    }

    /**
     * Generates a unique order ID.
     *
     * @return a new unique order ID
     */
    private String generateOrderId() {
        return orderPrefix + "-" + orderCounter.incrementAndGet();
    }

    /**
     * Creates a new order for a customer.
     *
     * @param customerId      the customer placing the order
     * @param shippingAddress the delivery address
     * @param items           the items to include in the order
     * @return the created order
     * @throws IllegalArgumentException if customerId is null/empty or items is empty
     */
    public Order createOrder(String customerId, String shippingAddress, List<OrderItem> items) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one item");
        }

        String orderId = generateOrderId();
        Order order = new Order(orderId, customerId, shippingAddress);

        for (OrderItem item : items) {
            order.addItem(item);
        }

        orders.put(orderId, order);
        return order;
    }

    /**
     * Retrieves an order by its ID.
     *
     * @param orderId the order ID to look up
     * @return an Optional containing the order if found, or empty if not
     */
    public Optional<Order> getOrderById(String orderId) {
        return Optional.ofNullable(orders.get(orderId));
    }

    /**
     * Retrieves all orders for a specific customer, sorted by creation date (newest first).
     *
     * @param customerId the customer ID to look up orders for
     * @return a list of orders for the customer
     */
    public List<Order> getOrdersByCustomerId(String customerId) {
        return orders.values().stream()
                .filter(order -> order.getCustomerId().equals(customerId))
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .collect(Collectors.toList());
    }

    /**
     * Updates the status of an order.
     *
     * @param orderId   the order to update
     * @param newStatus the target status
     * @param notes     optional notes about the transition
     * @return the updated order
     * @throws IllegalArgumentException if the order is not found
     * @throws IllegalStateException    if the transition is not valid
     */
    public Order updateOrderStatus(String orderId, OrderStatus newStatus, String notes) {
        Order order = orders.get(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Order not found: " + orderId);
        }
        order.updateStatus(newStatus, notes);
        return order;
    }

    /**
     * Cancels an order if it is in a cancellable state.
     *
     * @param orderId the order to cancel
     * @param reason  the reason for cancellation
     * @return the cancelled order
     * @throws IllegalArgumentException if the order is not found
     * @throws IllegalStateException    if the order cannot be cancelled from its current state
     */
    public Order cancelOrder(String orderId, String reason) {
        return updateOrderStatus(orderId, OrderStatus.CANCELLED, reason);
    }

    /**
     * Gets the full status change history for an order (tracking information).
     *
     * @param orderId the order to get history for
     * @return the list of status changes
     * @throws IllegalArgumentException if the order is not found
     */
    public List<OrderStatusChange> getOrderStatusHistory(String orderId) {
        Order order = orders.get(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Order not found: " + orderId);
        }
        return order.getStatusHistory();
    }

    /**
     * Searches for orders by status.
     *
     * @param status the status to filter by
     * @return a list of orders with the given status
     */
    public List<Order> getOrdersByStatus(OrderStatus status) {
        return orders.values().stream()
                .filter(order -> order.getStatus() == status)
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .collect(Collectors.toList());
    }

    /**
     * Searches for orders created within a date range.
     *
     * @param from the start of the date range (inclusive)
     * @param to   the end of the date range (inclusive)
     * @return a list of orders created within the range
     */
    public List<Order> getOrdersByDateRange(LocalDateTime from, LocalDateTime to) {
        return orders.values().stream()
                .filter(order -> !order.getCreatedAt().isBefore(from) && !order.getCreatedAt().isAfter(to))
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .collect(Collectors.toList());
    }

    /**
     * Gets a summary of order counts by status.
     *
     * @return a map of status to order count
     */
    public Map<OrderStatus, Long> getOrderSummary() {
        return orders.values().stream()
                .collect(Collectors.groupingBy(Order::getStatus, Collectors.counting()));
    }

    /**
     * Returns the total number of orders in the system.
     *
     * @return the order count
     */
    public int getTotalOrderCount() {
        return orders.size();
    }

    /**
     * Calculates the total revenue from all delivered orders.
     *
     * @return the total revenue
     */
    public double getTotalRevenue() {
        return orders.values().stream()
                .filter(order -> order.getStatus() == OrderStatus.DELIVERED)
                .mapToDouble(Order::getTotalAmount)
                .sum();
    }
}
