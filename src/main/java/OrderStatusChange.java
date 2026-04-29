import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Records a single status transition in an order's lifecycle.
 * Used for order tracking and audit trail purposes.
 */
public class OrderStatusChange {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final OrderStatus fromStatus;
    private final OrderStatus toStatus;
    private final LocalDateTime timestamp;
    private final String notes;

    /**
     * Creates a new status change record.
     *
     * @param fromStatus the previous status (null for initial creation)
     * @param toStatus   the new status
     * @param notes      optional notes about the status change
     */
    public OrderStatusChange(OrderStatus fromStatus, OrderStatus toStatus, String notes) {
        this.fromStatus = fromStatus;
        this.toStatus = toStatus;
        this.timestamp = LocalDateTime.now();
        this.notes = notes != null ? notes : "";
    }

    public OrderStatus getFromStatus() {
        return fromStatus;
    }

    public OrderStatus getToStatus() {
        return toStatus;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getNotes() {
        return notes;
    }

    @Override
    public String toString() {
        String from = (fromStatus != null) ? fromStatus.name() : "NEW";
        return String.format("[%s] %s -> %s%s",
                timestamp.format(FORMATTER),
                from,
                toStatus.name(),
                notes.isEmpty() ? "" : " (" + notes + ")");
    }
}
