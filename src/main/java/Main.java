/**
 * Main entry point for the Product Catalog application.
 * Starts the HTTP server to serve the product listing UI and REST API.
 */
public class Main {

    private static final int DEFAULT_PORT = 8080;

    public static void main(String[] args) {
        int port = DEFAULT_PORT;

        // Allow port override via command-line argument or environment variable
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Invalid port argument: " + args[0] + ". Using default port " + DEFAULT_PORT);
            }
        } else {
            String envPort = System.getenv("PORT");
            if (envPort != null && !envPort.isEmpty()) {
                try {
                    port = Integer.parseInt(envPort);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid PORT environment variable: " + envPort + ". Using default port " + DEFAULT_PORT);
                }
            }
        }

        try {
            ProductServer server = new ProductServer(port);
            server.start();

            // Register shutdown hook for graceful termination
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("\nShutting down Product Catalog Server...");
                server.stop();
                System.out.println("Server stopped.");
            }));
        } catch (Exception e) {
            System.err.println("Failed to start server: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
