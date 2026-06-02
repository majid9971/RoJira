import java.util.Optional;

/**
 * CustomerService provides business logic for customer operations.
 * It acts as a layer between the API endpoint and the repository.
 */
public class CustomerService {
    
    private final CustomerRepository repository;
    
    /**
     * Constructor with repository dependency
     * 
     * @param repository the customer repository
     */
    public CustomerService(CustomerRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException("Repository cannot be null");
        }
        this.repository = repository;
    }
    
    /**
     * Search for a customer by email.
     * 
     * @param email the customer email to search for
     * @return the Customer if found
     * @throws IllegalArgumentException if email is invalid
     * @throws CustomerNotFoundException if customer is not found
     */
    public Customer searchCustomerByEmail(String email) throws CustomerNotFoundException {
        // Validate email input
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        
        // Validate email format (basic validation)
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email format");
        }
        
        // Search in repository
        Optional<Customer> customer = repository.findByEmail(email);
        
        if (customer.isEmpty()) {
            throw new CustomerNotFoundException("Customer not found with email: " + email);
        }
        
        return customer.get();
    }
    
    /**
     * Get a customer by ID.
     * 
     * @param id the customer ID
     * @return the Customer if found
     * @throws IllegalArgumentException if id is invalid
     * @throws CustomerNotFoundException if customer is not found
     */
    public Customer getCustomerById(String id) throws CustomerNotFoundException {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID cannot be null or empty");
        }
        
        Optional<Customer> customer = repository.findById(id);
        
        if (customer.isEmpty()) {
            throw new CustomerNotFoundException("Customer not found with ID: " + id);
        }
        
        return customer.get();
    }
    
    /**
     * Create a new customer.
     * 
     * @param email customer email
     * @param name customer name
     * @return the created customer
     * @throws IllegalArgumentException if inputs are invalid
     */
    public Customer createCustomer(String email, String name) throws IllegalArgumentException {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email format");
        }
        
        // Check if customer with this email already exists
        try {
            searchCustomerByEmail(email);
            throw new IllegalArgumentException("Customer with email already exists");
        } catch (CustomerNotFoundException e) {
            // This is expected - customer doesn't exist yet
        }
        
        Customer customer = new Customer(email, name);
        return repository.save(customer);
    }
    
    /**
     * Update an existing customer.
     * 
     * @param id the customer ID
     * @param name the updated name
     * @param phone the updated phone
     * @param address the updated address
     * @return the updated customer
     * @throws CustomerNotFoundException if customer not found
     */
    public Customer updateCustomer(String id, String name, String phone, String address) 
            throws CustomerNotFoundException {
        Customer customer = getCustomerById(id);
        
        if (name != null && !name.trim().isEmpty()) {
            customer.setName(name);
        }
        
        if (phone != null && !phone.trim().isEmpty()) {
            customer.setPhone(phone);
        }
        
        if (address != null && !address.trim().isEmpty()) {
            customer.setAddress(address);
        }
        
        return repository.save(customer);
    }
    
    /**
     * Delete a customer.
     * 
     * @param id the customer ID
     * @return true if deleted, false if not found
     */
    public boolean deleteCustomer(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID cannot be null or empty");
        }
        
        return repository.deleteById(id);
    }
    
    /**
     * Validate email format using a simple regex pattern.
     * 
     * @param email the email to validate
     * @return true if email format is valid, false otherwise
     */
    private boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }
        
        // Simple email validation pattern
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(emailRegex);
    }
    
    /**
     * Custom exception for when customer is not found
     */
    public static class CustomerNotFoundException extends Exception {
        public CustomerNotFoundException(String message) {
            super(message);
        }
    }
}
