/**
 * CustomerSearchController provides REST API endpoints for customer search operations.
 * This controller handles HTTP requests for searching and managing customers.
 * 
 * Endpoints:
 * - GET /customers/search?email={email} - Search customer by email
 * - GET /customers/{id} - Get customer by ID
 * - POST /customers - Create new customer
 * - PUT /customers/{id} - Update customer
 * - DELETE /customers/{id} - Delete customer
 */
public class CustomerSearchController {
    
    private final CustomerService customerService;
    
    /**
     * Constructor with service dependency
     * 
     * @param customerService the customer service
     */
    public CustomerSearchController(CustomerService customerService) {
        if (customerService == null) {
            throw new IllegalArgumentException("CustomerService cannot be null");
        }
        this.customerService = customerService;
    }
    
    /**
     * Search customer by email.
     * 
     * Endpoint: GET /customers/search?email={email}
     * 
     * @param email the customer email to search for
     * @return ApiResponse with customer details if found
     * 
     * Response:
     * - 200 OK: Returns customer object
     * - 400 Bad Request: Email parameter missing or invalid
     * - 404 Not Found: Customer with email not found
     * - 500 Internal Server Error: Server error
     */
    public ApiResponse<Customer> searchByEmail(String email) {
        try {
            // Validate email parameter
            if (email == null || email.trim().isEmpty()) {
                return ApiResponse.badRequest("Email query parameter is required");
            }
            
            // Search for customer
            Customer customer = customerService.searchCustomerByEmail(email);
            return ApiResponse.ok("Customer found", customer);
            
        } catch (IllegalArgumentException e) {
            return ApiResponse.badRequest(e.getMessage());
        } catch (CustomerService.CustomerNotFoundException e) {
            return ApiResponse.notFound(e.getMessage());
        } catch (Exception e) {
            return ApiResponse.internalServerError("An unexpected error occurred: " + e.getMessage());
        }
    }
    
    /**
     * Get customer by ID.
     * 
     * Endpoint: GET /customers/{id}
     * 
     * @param id the customer ID
     * @return ApiResponse with customer details if found
     * 
     * Response:
     * - 200 OK: Returns customer object
     * - 400 Bad Request: ID parameter missing or invalid
     * - 404 Not Found: Customer with ID not found
     */
    public ApiResponse<Customer> getById(String id) {
        try {
            if (id == null || id.trim().isEmpty()) {
                return ApiResponse.badRequest("Customer ID is required");
            }
            
            Customer customer = customerService.getCustomerById(id);
            return ApiResponse.ok("Customer found", customer);
            
        } catch (IllegalArgumentException e) {
            return ApiResponse.badRequest(e.getMessage());
        } catch (CustomerService.CustomerNotFoundException e) {
            return ApiResponse.notFound(e.getMessage());
        } catch (Exception e) {
            return ApiResponse.internalServerError("An unexpected error occurred: " + e.getMessage());
        }
    }
    
    /**
     * Create a new customer.
     * 
     * Endpoint: POST /customers
     * 
     * @param email the customer email
     * @param name the customer name
     * @return ApiResponse with created customer
     * 
     * Response:
     * - 201 Created: Returns created customer object
     * - 400 Bad Request: Invalid or missing required fields
     */
    public ApiResponse<Customer> createCustomer(String email, String name) {
        try {
            Customer customer = customerService.createCustomer(email, name);
            return ApiResponse.created("Customer created successfully", customer);
            
        } catch (IllegalArgumentException e) {
            return ApiResponse.badRequest(e.getMessage());
        } catch (Exception e) {
            return ApiResponse.internalServerError("An unexpected error occurred: " + e.getMessage());
        }
    }
    
    /**
     * Update an existing customer.
     * 
     * Endpoint: PUT /customers/{id}
     * 
     * @param id the customer ID
     * @param name the updated name (optional)
     * @param phone the updated phone (optional)
     * @param address the updated address (optional)
     * @return ApiResponse with updated customer
     * 
     * Response:
     * - 200 OK: Returns updated customer object
     * - 400 Bad Request: Invalid ID or parameters
     * - 404 Not Found: Customer not found
     */
    public ApiResponse<Customer> updateCustomer(String id, String name, String phone, String address) {
        try {
            if (id == null || id.trim().isEmpty()) {
                return ApiResponse.badRequest("Customer ID is required");
            }
            
            Customer customer = customerService.updateCustomer(id, name, phone, address);
            return ApiResponse.ok("Customer updated successfully", customer);
            
        } catch (IllegalArgumentException e) {
            return ApiResponse.badRequest(e.getMessage());
        } catch (CustomerService.CustomerNotFoundException e) {
            return ApiResponse.notFound(e.getMessage());
        } catch (Exception e) {
            return ApiResponse.internalServerError("An unexpected error occurred: " + e.getMessage());
        }
    }
    
    /**
     * Delete a customer.
     * 
     * Endpoint: DELETE /customers/{id}
     * 
     * @param id the customer ID
     * @return ApiResponse indicating success or failure
     * 
     * Response:
     * - 200 OK: Customer deleted successfully
     * - 400 Bad Request: Invalid ID
     * - 404 Not Found: Customer not found
     */
    public ApiResponse<Void> deleteCustomer(String id) {
        try {
            if (id == null || id.trim().isEmpty()) {
                return ApiResponse.badRequest("Customer ID is required");
            }
            
            boolean deleted = customerService.deleteCustomer(id);
            
            if (!deleted) {
                return ApiResponse.notFound("Customer not found");
            }
            
            return ApiResponse.ok("Customer deleted successfully", null);
            
        } catch (IllegalArgumentException e) {
            return ApiResponse.badRequest(e.getMessage());
        } catch (Exception e) {
            return ApiResponse.internalServerError("An unexpected error occurred: " + e.getMessage());
        }
    }
    
    /**
     * Generic API Response class for consistent response format
     * 
     * @param <T> the type of data in the response
     */
    public static class ApiResponse<T> {
        private int statusCode;
        private String message;
        private T data;
        private boolean success;
        
        public ApiResponse(int statusCode, String message, T data) {
            this.statusCode = statusCode;
            this.message = message;
            this.data = data;
            this.success = statusCode >= 200 && statusCode < 300;
        }
        
        // Response factory methods
        public static <T> ApiResponse<T> ok(String message, T data) {
            return new ApiResponse<>(200, message, data);
        }
        
        public static <T> ApiResponse<T> created(String message, T data) {
            return new ApiResponse<>(201, message, data);
        }
        
        public static <T> ApiResponse<T> badRequest(String message) {
            return new ApiResponse<>(400, message, null);
        }
        
        public static <T> ApiResponse<T> notFound(String message) {
            return new ApiResponse<>(404, message, null);
        }
        
        public static <T> ApiResponse<T> internalServerError(String message) {
            return new ApiResponse<>(500, message, null);
        }
        
        // Getters
        public int getStatusCode() {
            return statusCode;
        }
        
        public String getMessage() {
            return message;
        }
        
        public T getData() {
            return data;
        }
        
        public boolean isSuccess() {
            return success;
        }
        
        @Override
        public String toString() {
            return "ApiResponse{" +
                    "statusCode=" + statusCode +
                    ", message='" + message + '\'' +
                    ", data=" + data +
                    ", success=" + success +
                    '}';
        }
    }
}
