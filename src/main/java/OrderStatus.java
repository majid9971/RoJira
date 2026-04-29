/**
 * Represents the various states an order can be in during its lifecycle.
 * Each status defines valid transitions to enforce business rules.
 */
public enum OrderStatus {
    PENDING("Order has been placed and is awaiting confirmation"),
    CONFIRMED("Order has been confirmed and is being prepared"),
    PROCESSING("Order is being processed and packed"),
    SHIPPED("Order has been shipped and is in transit"),
    DELIVERED("Order has been delivered to the customer"),
    CANCELLED("Order has been cancelled"),
    REFUNDED("Order has been refunded");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    /**
     * Gets the human-readable description of this status.
     *
     * @return the status description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Checks whether a transition from this status to the target status is valid.
     * Enforces the order lifecycle state machine.
     *
     * @param target the target status to transition to
     * @return true if the transition is valid, false otherwise
     */
    public boolean canTransitionTo(OrderStatus target) {
        if (this == target) {
            return false;
        }
        switch (this) {
            case PENDING:
                return target == CONFIRMED || target == CANCELLED;
            case CONFIRMED:
                return target == PROCESSING || target == CANCELLED;
            case PROCESSING:
                return target == SHIPPED || target == CANCELLED;
            case SHIPPED:
                return target == DELIVERED;
            case DELIVERED:
                return target == REFUNDED;
            case CANCELLED:
            case REFUNDED:
                return false; // Terminal states
            default:
                return false;
        }
    }
}
