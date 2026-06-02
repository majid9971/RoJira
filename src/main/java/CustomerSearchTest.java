/**
 * Unit tests for Customer Search API functionality.
 * Tests cover all acceptance criteria and edge cases.
 */
public class CustomerSearchTest {
    
    private CustomerRepository repository;
    private CustomerService service;
    private CustomerSearchController controller;
    
    /**
     * Setup test environment before each test
     */
    public void setUp() {
        repository = new CustomerRepository();
        service = new CustomerService(repository);
        controller = new CustomerSearchController(service);
    }
    
    /**
     * Test 1: Search customer successfully by email
     * Acceptance Criteria: Return customer details when email is found
     */
    public void testSearchCustomerByEmailSuccess() {
        System.out.println("\n=== Test 1: Search customer by email (Success Case) ===");
        
        setUp();
        
        try {
            // Use the existing sample customer
            CustomerSearchController.ApiResponse<Customer> response = 
                controller.searchByEmail("john.doe@example.com");
            
            // Verify response
            assert response.isSuccess() : "Response should be successful";
            assert response.getStatusCode() == 200 : "Status code should be 200";
            assert response.getData() != null : "Customer data should not be null";
            assert response.getData().getEmail().equals("john.doe@example.com") : 
                "Email should match search query";
            assert response.getData().getName().equals("John Doe") : 
                "Name should be correct";
            
            System.out.println("✓ Customer found successfully");
            System.out.println("  Email: " + response.getData().getEmail());
            System.out.println("  Name: " + response.getData().getName());
            System.out.println("  Status Code: " + response.getStatusCode());
            System.out.println("  Message: " + response.getMessage());
            
        } catch (Exception e) {
            System.out.println("✗ Test failed: " + e.getMessage());
            throw new AssertionError("Test failed", e);
        }
    }
    
    /**
     * Test 2: Search for non-existent customer returns 404
     * Acceptance Criteria: Return 404 when customer not found
     */
    public void testSearchCustomerNotFound() {
        System.out.println("\n=== Test 2: Search customer not found (404 Case) ===");
        
        setUp();
        
        CustomerSearchController.ApiResponse<Customer> response = 
            controller.searchByEmail("nonexistent@example.com");
        
        // Verify response
        assert !response.isSuccess() : "Response should not be successful";
        assert response.getStatusCode() == 404 : "Status code should be 404";
        assert response.getData() == null : "Customer data should be null";
        assert response.getMessage().contains("not found") : 
            "Message should indicate customer not found";
        
        System.out.println("✓ Correctly returned 404 for non-existent customer");
        System.out.println("  Status Code: " + response.getStatusCode());
        System.out.println("  Message: " + response.getMessage());
    }
    
    /**
     * Test 3: Search with empty email parameter
     * Acceptance Criteria: Handle invalid input gracefully
     */
    public void testSearchWithEmptyEmail() {
        System.out.println("\n=== Test 3: Search with empty email (Bad Request) ===");
        
        setUp();
        
        CustomerSearchController.ApiResponse<Customer> response = 
            controller.searchByEmail("");
        
        // Verify response
        assert !response.isSuccess() : "Response should not be successful";
        assert response.getStatusCode() == 400 : "Status code should be 400";
        assert response.getData() == null : "Customer data should be null";
        
        System.out.println("✓ Correctly handled empty email parameter");
        System.out.println("  Status Code: " + response.getStatusCode());
        System.out.println("  Message: " + response.getMessage());
    }
    
    /**
     * Test 4: Search with null email parameter
     * Acceptance Criteria: Handle null input gracefully
     */
    public void testSearchWithNullEmail() {
        System.out.println("\n=== Test 4: Search with null email (Bad Request) ===");
        
        setUp();
        
        CustomerSearchController.ApiResponse<Customer> response = 
            controller.searchByEmail(null);
        
        // Verify response
        assert !response.isSuccess() : "Response should not be successful";
        assert response.getStatusCode() == 400 : "Status code should be 400";
        assert response.getData() == null : "Customer data should be null";
        
        System.out.println("✓ Correctly handled null email parameter");
        System.out.println("  Status Code: " + response.getStatusCode());
        System.out.println("  Message: " + response.getMessage());
    }
    
    /**
     * Test 5: Case-insensitive email search
     * Acceptance Criteria: Email search should be case-insensitive
     */
    public void testCaseInsensitiveEmailSearch() {
        System.out.println("\n=== Test 5: Case-insensitive email search ===");
        
        setUp();
        
        // Try different cases
        String[] emailVariations = {
            "JOHN.DOE@EXAMPLE.COM",
            "John.Doe@Example.Com",
            "john.doe@example.com"
        };
        
        for (String email : emailVariations) {
            CustomerSearchController.ApiResponse<Customer> response = 
                controller.searchByEmail(email);
            
            assert response.isSuccess() : "Should find customer with email: " + email;
            assert response.getStatusCode() == 200 : "Status code should be 200";
            
            System.out.println("✓ Found customer with email: " + email);
        }
    }
    
    /**
     * Test 6: Get customer by ID
     * Acceptance Criteria: Retrieve customer by ID successfully
     */
    public void testGetCustomerById() {
        System.out.println("\n=== Test 6: Get customer by ID ===");
        
        setUp();
        
        CustomerSearchController.ApiResponse<Customer> response = 
            controller.getById("1");
        
        assert response.isSuccess() : "Response should be successful";
        assert response.getStatusCode() == 200 : "Status code should be 200";
        assert response.getData() != null : "Customer data should not be null";
        assert response.getData().getId().equals("1") : "ID should match";
        
        System.out.println("✓ Customer retrieved by ID successfully");
        System.out.println("  ID: " + response.getData().getId());
        System.out.println("  Name: " + response.getData().getName());
    }
    
    /**
     * Test 7: Get non-existent customer by ID returns 404
     */
    public void testGetNonExistentCustomerById() {
        System.out.println("\n=== Test 7: Get non-existent customer by ID (404) ===");
        
        setUp();
        
        CustomerSearchController.ApiResponse<Customer> response = 
            controller.getById("nonexistent-id");
        
        assert !response.isSuccess() : "Response should not be successful";
        assert response.getStatusCode() == 404 : "Status code should be 404";
        
        System.out.println("✓ Correctly returned 404 for non-existent ID");
        System.out.println("  Status Code: " + response.getStatusCode());
    }
    
    /**
     * Test 8: Create new customer
     * Acceptance Criteria: Successfully create a new customer
     */
    public void testCreateNewCustomer() {
        System.out.println("\n=== Test 8: Create new customer ===");
        
        setUp();
        
        CustomerSearchController.ApiResponse<Customer> response = 
            controller.createCustomer("newcustomer@example.com", "New Customer");
        
        assert response.isSuccess() : "Response should be successful";
        assert response.getStatusCode() == 201 : "Status code should be 201";
        assert response.getData() != null : "Customer data should not be null";
        assert response.getData().getEmail().equals("newcustomer@example.com") : 
            "Email should match";
        assert response.getData().getName().equals("New Customer") : 
            "Name should match";
        
        System.out.println("✓ New customer created successfully");
        System.out.println("  Email: " + response.getData().getEmail());
        System.out.println("  Name: " + response.getData().getName());
        System.out.println("  ID: " + response.getData().getId());
    }
    
    /**
     * Test 9: Update customer
     */
    public void testUpdateCustomer() {
        System.out.println("\n=== Test 9: Update customer ===");
        
        setUp();
        
        CustomerSearchController.ApiResponse<Customer> response = 
            controller.updateCustomer("1", "John Updated", "555-9999", "999 New St");
        
        assert response.isSuccess() : "Response should be successful";
        assert response.getStatusCode() == 200 : "Status code should be 200";
        assert response.getData().getName().equals("John Updated") : 
            "Name should be updated";
        assert response.getData().getPhone().equals("555-9999") : 
            "Phone should be updated";
        
        System.out.println("✓ Customer updated successfully");
        System.out.println("  Updated Name: " + response.getData().getName());
        System.out.println("  Updated Phone: " + response.getData().getPhone());
    }
    
    /**
     * Test 10: Delete customer
     */
    public void testDeleteCustomer() {
        System.out.println("\n=== Test 10: Delete customer ===");
        
        setUp();
        
        CustomerSearchController.ApiResponse<Void> response = 
            controller.deleteCustomer("1");
        
        assert response.isSuccess() : "Response should be successful";
        assert response.getStatusCode() == 200 : "Status code should be 200";
        
        // Verify customer is deleted
        CustomerSearchController.ApiResponse<Customer> getResponse = 
            controller.getById("1");
        assert getResponse.getStatusCode() == 404 : "Customer should not exist after deletion";
        
        System.out.println("✓ Customer deleted successfully");
    }
    
    /**
     * Test 11: Invalid email format
     */
    public void testInvalidEmailFormat() {
        System.out.println("\n=== Test 11: Create customer with invalid email ===");
        
        setUp();
        
        CustomerSearchController.ApiResponse<Customer> response = 
            controller.createCustomer("invalid-email", "Test Customer");
        
        assert !response.isSuccess() : "Response should not be successful";
        assert response.getStatusCode() == 400 : "Status code should be 400";
        assert response.getMessage().contains("Invalid email") : 
            "Message should mention invalid email";
        
        System.out.println("✓ Correctly rejected invalid email format");
        System.out.println("  Message: " + response.getMessage());
    }
    
    /**
     * Test 12: Email search with whitespace
     */
    public void testEmailSearchWithWhitespace() {
        System.out.println("\n=== Test 12: Email search with whitespace ===");
        
        setUp();
        
        CustomerSearchController.ApiResponse<Customer> response = 
            controller.searchByEmail("  john.doe@example.com  ");
        
        assert response.isSuccess() : "Should trim and find customer";
        assert response.getStatusCode() == 200 : "Status code should be 200";
        
        System.out.println("✓ Successfully trimmed whitespace from email");
    }
    
    /**
     * Run all tests
     */
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════╗");
        System.out.println("║   Customer Search API - Unit Tests                 ║");
        System.out.println("╚════════════════════════════════════════════════════╝");
        
        CustomerSearchTest tests = new CustomerSearchTest();
        
        try {
            tests.testSearchCustomerByEmailSuccess();
            tests.testSearchCustomerNotFound();
            tests.testSearchWithEmptyEmail();
            tests.testSearchWithNullEmail();
            tests.testCaseInsensitiveEmailSearch();
            tests.testGetCustomerById();
            tests.testGetNonExistentCustomerById();
            tests.testCreateNewCustomer();
            tests.testUpdateCustomer();
            tests.testDeleteCustomer();
            tests.testInvalidEmailFormat();
            tests.testEmailSearchWithWhitespace();
            
            System.out.println("\n╔════════════════════════════════════════════════════╗");
            System.out.println("║         ✓ All 12 tests passed successfully!        ║");
            System.out.println("╚════════════════════════════════════════════════════╝\n");
            
        } catch (AssertionError e) {
            System.out.println("\n✗ Test execution failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
