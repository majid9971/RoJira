import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * CustomerRepository handles data access for Customer entities.
 * Currently uses an in-memory HashMap for storage.
 * Can be extended to use a database (SQL, NoSQL, etc.)
 */
public class CustomerRepository {
    
    // In-memory storage for customers
    private static final Map<String, Customer> customers = new HashMap<>();
    
    // Static block to initialize with sample data
    static {
        initializeSampleData();
    }
    
    /**
     * Initialize repository with sample customer data
     */
    private static void initializeSampleData() {
        // Sample customers for testing
        Customer customer1 = new Customer("1", "john.doe@example.com", "John Doe", "555-0101", "123 Main St");
        Customer customer2 = new Customer("2", "jane.smith@example.com", "Jane Smith", "555-0102", "456 Oak Ave");
        Customer customer3 = new Customer("3", "bob.wilson@example.com", "Bob Wilson", "555-0103", "789 Pine Rd");
        
        customers.put(customer1.getId(), customer1);
        customers.put(customer2.getId(), customer2);
        customers.put(customer3.getId(), customer3);
    }
    
    /**
     * Search customer by email.
     * Email search is case-insensitive.
     * 
     * @param email the customer email to search for
     * @return Optional containing the Customer if found, empty otherwise
     * @throws IllegalArgumentException if email is null or empty
     */
    public Optional<Customer> findByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        
        String normalizedEmail = email.trim().toLowerCase();
        
        return customers.values().stream()
                .filter(customer -> customer.getEmail() != null && 
                        customer.getEmail().toLowerCase().equals(normalizedEmail))
                .findFirst();
    }
    
    /**
     * Search customer by ID.
     * 
     * @param id the customer ID to search for
     * @return Optional containing the Customer if found, empty otherwise
     * @throws IllegalArgumentException if id is null or empty
     */
    public Optional<Customer> findById(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID cannot be null or empty");
        }
        
        return Optional.ofNullable(customers.get(id));
    }
    
    /**
     * Save or update a customer.
     * 
     * @param customer the customer to save
     * @return the saved customer
     * @throws IllegalArgumentException if customer is null
     */
    public Customer save(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null");
        }
        
        if (customer.getId() == null) {
            customer.setId(java.util.UUID.randomUUID().toString());
        }
        
        customers.put(customer.getId(), customer);
        return customer;
    }
    
    /**
     * Delete a customer by ID.
     * 
     * @param id the customer ID to delete
     * @return true if customer was deleted, false if not found
     */
    public boolean deleteById(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID cannot be null or empty");
        }
        
        return customers.remove(id) != null;
    }
    
    /**
     * Get all customers.
     * 
     * @return a map of all customers
     */
    public Map<String, Customer> findAll() {
        return new HashMap<>(customers);
    }
    
    /**
     * Clear all customers (useful for testing).
     */
    public void clear() {
        customers.clear();
    }
    
    /**
     * Get total number of customers.
     * 
     * @return the count of customers
     */
    public int count() {
        return customers.size();
    }
}
