# RoJira

## Basic Calculator

This repository contains a simple Java calculator that performs basic arithmetic operations.

### Features

The calculator supports the following operations:
- **Addition**: Add two numbers
- **Subtraction**: Subtract one number from another
- **Multiplication**: Multiply two numbers
- **Division**: Divide one number by another (with divide-by-zero protection)
- **Modulo**: Calculate the remainder of division
- **Power**: Raise a number to a power
- **Square Root**: Calculate the square root of a number

### Project Structure

```
src/
└── main/
    └── java/
        ├── Calculator.java  # Calculator class with arithmetic operations
        └── Main.java        # Demo application showing calculator usage
```

### How to Use

#### Compile the code:
```bash
cd src/main/java
javac Calculator.java Main.java
```

#### Run the demo:
```bash
java Main
```

#### Use in your own code:
```java
Calculator calculator = new Calculator();

// Perform calculations
double sum = calculator.add(10, 5);        // 15.0
double diff = calculator.subtract(10, 5);  // 5.0
double product = calculator.multiply(10, 5); // 50.0
double quotient = calculator.divide(10, 5);  // 2.0
double remainder = calculator.modulo(10, 5); // 0.0
double power = calculator.power(2, 3);       // 8.0
double sqrt = calculator.squareRoot(16);     // 4.0
```

### Error Handling

The calculator includes proper error handling:
- **Division by zero**: Throws `ArithmeticException`
- **Negative square root**: Throws `IllegalArgumentException`

Example:
```java
try {
    calculator.divide(10, 0);
} catch (ArithmeticException e) {
    System.out.println("Error: " + e.getMessage());
}
```

---

## Order Management System

An order management system with order tracking and history features, part of the Shopping Module.

### Features

- **Order Creation**: Create orders with multiple items, customer info, and shipping address
- **Order Tracking**: Track orders through their full lifecycle (Pending → Confirmed → Processing → Shipped → Delivered)
- **Status History**: Full audit trail of all status changes with timestamps and notes
- **Order History**: Retrieve all orders for a customer, sorted by date
- **Order Cancellation**: Cancel orders with reason tracking (from valid states only)
- **Search & Filter**: Find orders by status or date range
- **Reporting**: Order summaries, counts by status, and revenue tracking

### Order Lifecycle

```
PENDING → CONFIRMED → PROCESSING → SHIPPED → DELIVERED → REFUNDED
   ↓          ↓            ↓
CANCELLED  CANCELLED   CANCELLED
```

### Project Structure

```
src/
└── main/
    └── java/
        ├── OrderStatus.java          # Order lifecycle states enum
        ├── OrderItem.java            # Individual item in an order
        ├── OrderStatusChange.java    # Status transition tracking record
        ├── Order.java                # Core order entity
        ├── OrderService.java         # Business logic & order management
        └── OrderManagementDemo.java  # Demo application
```

### Quick Start

#### Compile:
```bash
cd src/main/java
javac OrderStatus.java OrderItem.java OrderStatusChange.java Order.java OrderService.java OrderManagementDemo.java
```

#### Run the demo:
```bash
java OrderManagementDemo
```

### Usage Example

```java
OrderService orderService = new OrderService();

// Create an order
Order order = orderService.createOrder(
    "CUST-001",
    "123 Main St, Springfield, IL",
    Arrays.asList(
        new OrderItem("PROD-001", "Wireless Mouse", 2, 29.99),
        new OrderItem("PROD-002", "USB-C Hub", 1, 49.99)
    )
);

// Update order status with tracking notes
orderService.updateOrderStatus(order.getOrderId(), OrderStatus.CONFIRMED, "Payment verified");
orderService.updateOrderStatus(order.getOrderId(), OrderStatus.SHIPPED, "Tracking #FX123456");

// View tracking history
List<OrderStatusChange> history = orderService.getOrderStatusHistory(order.getOrderId());
history.forEach(System.out::println);

// Get customer order history
List<Order> customerOrders = orderService.getOrdersByCustomerId("CUST-001");

// Cancel an order
orderService.cancelOrder(orderId, "Customer changed mind");

// Get order summary
Map<OrderStatus, Long> summary = orderService.getOrderSummary();
```