import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Demonstrates the Order Management System functionality including
 * order creation, tracking, status updates, history, and reporting.
 */
public class OrderManagementDemo {

    public static void main(String[] args) {
        OrderService orderService = new OrderService();

        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║          ORDER MANAGEMENT SYSTEM - DEMO                     ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝\n");

        // ── 1. Create Orders ─────────────────────────────────────────
        System.out.println("=== 1. Creating Orders ===\n");

        Order order1 = orderService.createOrder(
                "CUST-001",
                "123 Main St, Springfield, IL 62701",
                Arrays.asList(
                        new OrderItem("PROD-001", "Wireless Mouse", 2, 29.99),
                        new OrderItem("PROD-002", "USB-C Hub", 1, 49.99),
                        new OrderItem("PROD-003", "Laptop Stand", 1, 35.00)
                )
        );
        System.out.println("Created Order #1:");
        System.out.println(order1);

        Order order2 = orderService.createOrder(
                "CUST-002",
                "456 Oak Ave, Portland, OR 97201",
                Arrays.asList(
                        new OrderItem("PROD-004", "Mechanical Keyboard", 1, 89.99),
                        new OrderItem("PROD-005", "Monitor Light Bar", 1, 45.00)
                )
        );
        System.out.println("Created Order #2:");
        System.out.println(order2);

        Order order3 = orderService.createOrder(
                "CUST-001",
                "123 Main St, Springfield, IL 62701",
                Arrays.asList(
                        new OrderItem("PROD-006", "Webcam HD 1080p", 1, 59.99),
                        new OrderItem("PROD-007", "Ring Light", 1, 25.00)
                )
        );
        System.out.println("Created Order #3 (same customer as #1):");
        System.out.println(order3);

        // ── 2. Order Tracking - Status Updates ───────────────────────
        System.out.println("=== 2. Order Tracking - Status Updates ===\n");

        System.out.println("Progressing Order #1 through its lifecycle...\n");

        orderService.updateOrderStatus(order1.getOrderId(), OrderStatus.CONFIRMED,
                "Payment verified via credit card");
        System.out.println("  ✓ Order confirmed");

        orderService.updateOrderStatus(order1.getOrderId(), OrderStatus.PROCESSING,
                "Items picked from warehouse");
        System.out.println("  ✓ Order processing");

        orderService.updateOrderStatus(order1.getOrderId(), OrderStatus.SHIPPED,
                "Shipped via FedEx - Tracking #FX123456789");
        System.out.println("  ✓ Order shipped");

        orderService.updateOrderStatus(order1.getOrderId(), OrderStatus.DELIVERED,
                "Delivered - Signed by J. Smith");
        System.out.println("  ✓ Order delivered");

        System.out.println("\nOrder #1 after all updates:");
        System.out.println(order1);

        // ── 3. Order Status History (Tracking) ───────────────────────
        System.out.println("=== 3. Order Status History (Tracking) ===\n");

        System.out.println("Full tracking history for Order " + order1.getOrderId() + ":");
        List<OrderStatusChange> history = orderService.getOrderStatusHistory(order1.getOrderId());
        for (OrderStatusChange change : history) {
            System.out.println("  " + change);
        }
        System.out.println();

        // ── 4. Order Cancellation ────────────────────────────────────
        System.out.println("=== 4. Order Cancellation ===\n");

        orderService.updateOrderStatus(order2.getOrderId(), OrderStatus.CONFIRMED,
                "Payment confirmed");
        System.out.println("Order #2 confirmed, now cancelling...");

        orderService.cancelOrder(order2.getOrderId(), "Customer requested cancellation - changed mind");
        System.out.println("  ✓ Order #2 cancelled");
        System.out.println("\nOrder #2 tracking history:");
        for (OrderStatusChange change : orderService.getOrderStatusHistory(order2.getOrderId())) {
            System.out.println("  " + change);
        }
        System.out.println();

        // ── 5. Customer Order History ────────────────────────────────
        System.out.println("=== 5. Customer Order History ===\n");

        List<Order> customerOrders = orderService.getOrdersByCustomerId("CUST-001");
        System.out.println("Orders for CUST-001 (" + customerOrders.size() + " orders):");
        for (Order order : customerOrders) {
            System.out.printf("  - %s | Status: %-12s | Total: $%.2f | Created: %s%n",
                    order.getOrderId(),
                    order.getStatus(),
                    order.getTotalAmount(),
                    order.getCreatedAt().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        }
        System.out.println();

        // ── 6. Search Orders by Status ───────────────────────────────
        System.out.println("=== 6. Search Orders by Status ===\n");

        for (OrderStatus status : OrderStatus.values()) {
            List<Order> byStatus = orderService.getOrdersByStatus(status);
            if (!byStatus.isEmpty()) {
                System.out.println(status + " (" + byStatus.size() + "):");
                for (Order o : byStatus) {
                    System.out.printf("    %s - Customer: %s - Total: $%.2f%n",
                            o.getOrderId(), o.getCustomerId(), o.getTotalAmount());
                }
            }
        }
        System.out.println();

        // ── 7. Order Lookup ──────────────────────────────────────────
        System.out.println("=== 7. Order Lookup ===\n");

        Optional<Order> found = orderService.getOrderById(order1.getOrderId());
        found.ifPresent(o -> System.out.println("Found order: " + o.getOrderId()
                + " | Status: " + o.getStatus()
                + " | Total: $" + String.format("%.2f", o.getTotalAmount())));

        Optional<Order> notFound = orderService.getOrderById("ORD-NONEXISTENT");
        System.out.println("Lookup ORD-NONEXISTENT: " + (notFound.isPresent() ? "Found" : "Not found"));
        System.out.println();

        // ── 8. Order Summary / Reporting ─────────────────────────────
        System.out.println("=== 8. Order Summary / Reporting ===\n");

        System.out.println("Total orders: " + orderService.getTotalOrderCount());
        System.out.printf("Total revenue (delivered): $%.2f%n", orderService.getTotalRevenue());

        Map<OrderStatus, Long> summary = orderService.getOrderSummary();
        System.out.println("\nOrders by status:");
        for (Map.Entry<OrderStatus, Long> entry : summary.entrySet()) {
            System.out.printf("  %-12s: %d%n", entry.getKey(), entry.getValue());
        }
        System.out.println();

        // ── 9. Error Handling ────────────────────────────────────────
        System.out.println("=== 9. Error Handling ===\n");

        // Try invalid status transition
        try {
            orderService.updateOrderStatus(order1.getOrderId(), OrderStatus.PENDING, "Try going back");
        } catch (IllegalStateException e) {
            System.out.println("✗ Expected error (invalid transition): " + e.getMessage());
        }

        // Try cancelling a delivered order
        try {
            orderService.cancelOrder(order1.getOrderId(), "Too late");
        } catch (IllegalStateException e) {
            System.out.println("✗ Expected error (cancel delivered): " + e.getMessage());
        }

        // Try looking up non-existent order for status history
        try {
            orderService.getOrderStatusHistory("ORD-FAKE");
        } catch (IllegalArgumentException e) {
            System.out.println("✗ Expected error (order not found): " + e.getMessage());
        }

        // Try creating order with no items
        try {
            orderService.createOrder("CUST-003", "789 Elm St", Arrays.asList());
        } catch (IllegalArgumentException e) {
            System.out.println("✗ Expected error (empty items): " + e.getMessage());
        }

        System.out.println("\n╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║          ORDER MANAGEMENT DEMO COMPLETE                     ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
    }
}
