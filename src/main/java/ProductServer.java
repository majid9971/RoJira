import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Lightweight HTTP server for the product catalog application.
 * Serves both the REST API and the product listing UI.
 */
public class ProductServer {

    private final HttpServer server;
    private final ProductService productService;

    public ProductServer(int port) throws IOException {
        ProductRepository repository = new ProductRepository();
        this.productService = new ProductService(repository);
        this.server = HttpServer.create(new InetSocketAddress(port), 0);
        registerRoutes();
    }

    private void registerRoutes() {
        server.createContext("/", new ProductPageHandler());
        server.createContext("/api/products", new ProductApiHandler());
        server.createContext("/api/categories", new CategoryApiHandler());
        server.createContext("/health", new HealthHandler());
    }

    public void start() {
        server.start();
        System.out.println("Product Catalog Server started on port " + server.getAddress().getPort());
        System.out.println("  UI:         http://localhost:" + server.getAddress().getPort() + "/");
        System.out.println("  API:        http://localhost:" + server.getAddress().getPort() + "/api/products");
        System.out.println("  Categories: http://localhost:" + server.getAddress().getPort() + "/api/categories");
        System.out.println("  Health:     http://localhost:" + server.getAddress().getPort() + "/health");
    }

    public void stop() {
        server.stop(0);
    }

    // ── Utility methods ─────────────────────────────────────────

    private static Map<String, String> parseQueryParams(String query) {
        Map<String, String> params = new HashMap<>();
        if (query == null || query.isEmpty()) return params;
        for (String pair : query.split("&")) {
            String[] kv = pair.split("=", 2);
            String key = URLDecoder.decode(kv[0], StandardCharsets.UTF_8);
            String value = kv.length > 1 ? URLDecoder.decode(kv[1], StandardCharsets.UTF_8) : "";
            params.put(key, value);
        }
        return params;
    }

    private static void sendResponse(HttpExchange exchange, int status, String contentType, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", contentType);
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    private static int parseIntParam(Map<String, String> params, String key, int defaultValue) {
        String val = params.get(key);
        if (val == null || val.isEmpty()) return defaultValue;
        try {
            return Integer.parseInt(val);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    // ── Handlers ────────────────────────────────────────────────

    /**
     * GET /health — health check endpoint.
     */
    private static class HealthHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendResponse(exchange, 405, "application/json", "{\"error\":\"Method not allowed\"}");
                return;
            }
            sendResponse(exchange, 200, "application/json", "{\"status\":\"UP\"}");
        }
    }

    /**
     * GET /api/categories — returns all product categories.
     */
    private class CategoryApiHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendResponse(exchange, 405, "application/json", "{\"error\":\"Method not allowed\"}");
                return;
            }
            List<String> categories = productService.getCategories();
            StringBuilder json = new StringBuilder("[");
            for (int i = 0; i < categories.size(); i++) {
                if (i > 0) json.append(",");
                json.append("\"").append(categories.get(i)).append("\"");
            }
            json.append("]");
            sendResponse(exchange, 200, "application/json", json.toString());
        }
    }

    /**
     * GET /api/products — returns paginated, filterable, sortable product list.
     * Query params: category, search, sortBy, sortOrder, page, pageSize
     *
     * GET /api/products/{id} — returns a single product by ID.
     */
    private class ProductApiHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendResponse(exchange, 405, "application/json", "{\"error\":\"Method not allowed\"}");
                return;
            }

            String path = exchange.getRequestURI().getPath();

            // Check if requesting a single product: /api/products/{id}
            if (path.length() > "/api/products/".length() && path.startsWith("/api/products/")) {
                String productId = path.substring("/api/products/".length());
                Optional<Product> product = productService.getProductById(productId);
                if (product.isPresent()) {
                    sendResponse(exchange, 200, "application/json", product.get().toJson());
                } else {
                    sendResponse(exchange, 404, "application/json", "{\"error\":\"Product not found\"}");
                }
                return;
            }

            // List products with filtering/sorting/pagination
            Map<String, String> params = parseQueryParams(exchange.getRequestURI().getQuery());
            String category = params.get("category");
            String search = params.get("search");
            String sortBy = params.getOrDefault("sortBy", "name");
            String sortOrder = params.getOrDefault("sortOrder", "asc");
            int page = parseIntParam(params, "page", 1);
            int pageSize = parseIntParam(params, "pageSize", 12);

            ProductService.ProductPage result = productService.getProducts(
                    category, search, sortBy, sortOrder, page, pageSize);
            sendResponse(exchange, 200, "application/json", result.toJson());
        }
    }

    /**
     * GET / or /products — serves the product listing HTML page.
     */
    private class ProductPageHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            if (!"/".equals(path) && !"/products".equals(path)) {
                sendResponse(exchange, 404, "text/html", "<h1>404 Not Found</h1>");
                return;
            }
            if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendResponse(exchange, 405, "text/html", "<h1>405 Method Not Allowed</h1>");
                return;
            }
            sendResponse(exchange, 200, "text/html; charset=UTF-8", buildProductListingPage());
        }
    }

    // ── HTML UI ─────────────────────────────────────────────────

    private String buildProductListingPage() {
        return "<!DOCTYPE html>\n"
+ "<html lang=\"en\">\n"
+ "<head>\n"
+ "  <meta charset=\"UTF-8\">\n"
+ "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
+ "  <title>Product Catalog</title>\n"
+ "  <style>\n"
+ "    *, *::before, *::after { box-sizing: border-box; margin: 0; padding: 0; }\n"
+ "    body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, sans-serif;\n"
+ "           background: #f0f2f5; color: #333; line-height: 1.6; }\n"
+ "    header { background: linear-gradient(135deg, #1a73e8, #0d47a1); color: white; padding: 1.5rem 2rem;\n"
+ "             box-shadow: 0 2px 8px rgba(0,0,0,0.15); }\n"
+ "    header h1 { font-size: 1.8rem; font-weight: 600; }\n"
+ "    header p { opacity: 0.85; margin-top: 0.25rem; }\n"
+ "    .container { max-width: 1280px; margin: 0 auto; padding: 1.5rem; }\n"
+ "\n"
+ "    /* Toolbar */\n"
+ "    .toolbar { display: flex; flex-wrap: wrap; gap: 0.75rem; margin-bottom: 1.5rem;\n"
+ "               background: white; padding: 1rem 1.25rem; border-radius: 10px;\n"
+ "               box-shadow: 0 1px 4px rgba(0,0,0,0.08); align-items: center; }\n"
+ "    .toolbar input[type=\"text\"] { flex: 1; min-width: 200px; padding: 0.6rem 1rem;\n"
+ "               border: 1px solid #ddd; border-radius: 6px; font-size: 0.95rem;\n"
+ "               transition: border-color 0.2s; }\n"
+ "    .toolbar input[type=\"text\"]:focus { outline: none; border-color: #1a73e8; }\n"
+ "    .toolbar select { padding: 0.6rem 1rem; border: 1px solid #ddd; border-radius: 6px;\n"
+ "               font-size: 0.95rem; background: white; cursor: pointer; }\n"
+ "    .toolbar select:focus { outline: none; border-color: #1a73e8; }\n"
+ "    .result-count { font-size: 0.9rem; color: #666; margin-left: auto; white-space: nowrap; }\n"
+ "\n"
+ "    /* Product grid */\n"
+ "    .product-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));\n"
+ "                    gap: 1.25rem; }\n"
+ "    .product-card { background: white; border-radius: 10px; overflow: hidden;\n"
+ "                    box-shadow: 0 1px 4px rgba(0,0,0,0.08); transition: transform 0.2s, box-shadow 0.2s;\n"
+ "                    display: flex; flex-direction: column; }\n"
+ "    .product-card:hover { transform: translateY(-3px); box-shadow: 0 6px 20px rgba(0,0,0,0.12); }\n"
+ "    .product-card img { width: 100%; height: 200px; object-fit: cover; }\n"
+ "    .product-info { padding: 1rem 1.25rem; flex: 1; display: flex; flex-direction: column; }\n"
+ "    .product-category { font-size: 0.75rem; text-transform: uppercase; letter-spacing: 0.05em;\n"
+ "                        color: #1a73e8; font-weight: 600; margin-bottom: 0.35rem; }\n"
+ "    .product-name { font-size: 1.05rem; font-weight: 600; margin-bottom: 0.4rem; }\n"
+ "    .product-desc { font-size: 0.85rem; color: #666; margin-bottom: 0.75rem; flex: 1;\n"
+ "                    display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical;\n"
+ "                    overflow: hidden; }\n"
+ "    .product-footer { display: flex; justify-content: space-between; align-items: center;\n"
+ "                      border-top: 1px solid #f0f0f0; padding-top: 0.75rem; }\n"
+ "    .product-price { font-size: 1.2rem; font-weight: 700; color: #0d47a1; }\n"
+ "    .product-rating { font-size: 0.85rem; color: #f5a623; }\n"
+ "    .product-stock { font-size: 0.75rem; margin-top: 0.5rem; }\n"
+ "    .in-stock { color: #2e7d32; }\n"
+ "    .out-of-stock { color: #c62828; font-weight: 600; }\n"
+ "\n"
+ "    /* Pagination */\n"
+ "    .pagination { display: flex; justify-content: center; align-items: center; gap: 0.5rem;\n"
+ "                  margin-top: 2rem; }\n"
+ "    .pagination button { padding: 0.5rem 1rem; border: 1px solid #ddd; border-radius: 6px;\n"
+ "                         background: white; cursor: pointer; font-size: 0.9rem; transition: all 0.2s; }\n"
+ "    .pagination button:hover:not(:disabled) { background: #1a73e8; color: white; border-color: #1a73e8; }\n"
+ "    .pagination button:disabled { opacity: 0.4; cursor: not-allowed; }\n"
+ "    .pagination .page-info { font-size: 0.9rem; color: #666; padding: 0 0.5rem; }\n"
+ "\n"
+ "    /* Loading & empty states */\n"
+ "    .loading { text-align: center; padding: 3rem; color: #666; font-size: 1.1rem; }\n"
+ "    .empty-state { text-align: center; padding: 3rem; color: #999; }\n"
+ "    .empty-state h2 { font-size: 1.3rem; margin-bottom: 0.5rem; color: #666; }\n"
+ "\n"
+ "    @media (max-width: 600px) {\n"
+ "      .toolbar { flex-direction: column; }\n"
+ "      .product-grid { grid-template-columns: 1fr; }\n"
+ "      header h1 { font-size: 1.4rem; }\n"
+ "    }\n"
+ "  </style>\n"
+ "</head>\n"
+ "<body>\n"
+ "  <header>\n"
+ "    <h1>&#128722; Product Catalog</h1>\n"
+ "    <p>Browse our curated collection of products</p>\n"
+ "  </header>\n"
+ "\n"
+ "  <div class=\"container\">\n"
+ "    <div class=\"toolbar\">\n"
+ "      <input type=\"text\" id=\"searchInput\" placeholder=\"Search products...\" />\n"
+ "      <select id=\"categoryFilter\">\n"
+ "        <option value=\"\">All Categories</option>\n"
+ "      </select>\n"
+ "      <select id=\"sortBy\">\n"
+ "        <option value=\"name\">Sort by Name</option>\n"
+ "        <option value=\"price\">Sort by Price</option>\n"
+ "        <option value=\"rating\">Sort by Rating</option>\n"
+ "      </select>\n"
+ "      <select id=\"sortOrder\">\n"
+ "        <option value=\"asc\">Ascending</option>\n"
+ "        <option value=\"desc\">Descending</option>\n"
+ "      </select>\n"
+ "      <span class=\"result-count\" id=\"resultCount\"></span>\n"
+ "    </div>\n"
+ "\n"
+ "    <div id=\"productGrid\" class=\"product-grid\">\n"
+ "      <div class=\"loading\">Loading products...</div>\n"
+ "    </div>\n"
+ "\n"
+ "    <div class=\"pagination\" id=\"pagination\"></div>\n"
+ "  </div>\n"
+ "\n"
+ "  <script>\n"
+ "    let currentPage = 1;\n"
+ "    const pageSize = 12;\n"
+ "\n"
+ "    // Load categories on startup\n"
+ "    async function loadCategories() {\n"
+ "      try {\n"
+ "        const res = await fetch('/api/categories');\n"
+ "        const categories = await res.json();\n"
+ "        const sel = document.getElementById('categoryFilter');\n"
+ "        categories.forEach(function(cat) {\n"
+ "          const opt = document.createElement('option');\n"
+ "          opt.value = cat;\n"
+ "          opt.textContent = cat;\n"
+ "          sel.appendChild(opt);\n"
+ "        });\n"
+ "      } catch (e) { console.error('Failed to load categories', e); }\n"
+ "    }\n"
+ "\n"
+ "    // Fetch and render products\n"
+ "    async function loadProducts() {\n"
+ "      const search = document.getElementById('searchInput').value;\n"
+ "      const category = document.getElementById('categoryFilter').value;\n"
+ "      const sortBy = document.getElementById('sortBy').value;\n"
+ "      const sortOrder = document.getElementById('sortOrder').value;\n"
+ "\n"
+ "      const params = new URLSearchParams({\n"
+ "        page: currentPage, pageSize: pageSize,\n"
+ "        sortBy: sortBy, sortOrder: sortOrder\n"
+ "      });\n"
+ "      if (search) params.set('search', search);\n"
+ "      if (category) params.set('category', category);\n"
+ "\n"
+ "      try {\n"
+ "        const res = await fetch('/api/products?' + params.toString());\n"
+ "        const data = await res.json();\n"
+ "        renderProducts(data);\n"
+ "        renderPagination(data);\n"
+ "        document.getElementById('resultCount').textContent =\n"
+ "          data.totalItems + ' product' + (data.totalItems !== 1 ? 's' : '') + ' found';\n"
+ "      } catch (e) {\n"
+ "        document.getElementById('productGrid').innerHTML =\n"
+ "          '<div class=\"empty-state\"><h2>Error loading products</h2><p>Please try again later.</p></div>';\n"
+ "      }\n"
+ "    }\n"
+ "\n"
+ "    function renderProducts(data) {\n"
+ "      const grid = document.getElementById('productGrid');\n"
+ "      if (!data.items || data.items.length === 0) {\n"
+ "        grid.innerHTML = '<div class=\"empty-state\"><h2>No products found</h2>'\n"
+ "          + '<p>Try adjusting your search or filters.</p></div>';\n"
+ "        return;\n"
+ "      }\n"
+ "      grid.innerHTML = data.items.map(function(p) {\n"
+ "        var stars = '\\u2605'.repeat(Math.round(p.rating)) + '\\u2606'.repeat(5 - Math.round(p.rating));\n"
+ "        var stockClass = p.inStock ? 'in-stock' : 'out-of-stock';\n"
+ "        var stockText = p.inStock ? 'In Stock (' + p.stockQuantity + ')' : 'Out of Stock';\n"
+ "        return '<div class=\"product-card\">'\n"
+ "          + '<img src=\"' + p.imageUrl + '\" alt=\"' + p.name + '\" />'\n"
+ "          + '<div class=\"product-info\">'\n"
+ "          + '  <div class=\"product-category\">' + p.category + '</div>'\n"
+ "          + '  <div class=\"product-name\">' + p.name + '</div>'\n"
+ "          + '  <div class=\"product-desc\">' + p.description + '</div>'\n"
+ "          + '  <div class=\"product-footer\">'\n"
+ "          + '    <span class=\"product-price\">$' + p.price.toFixed(2) + '</span>'\n"
+ "          + '    <span class=\"product-rating\">' + stars + ' ' + p.rating.toFixed(1) + '</span>'\n"
+ "          + '  </div>'\n"
+ "          + '  <div class=\"product-stock ' + stockClass + '\">' + stockText + '</div>'\n"
+ "          + '</div></div>';\n"
+ "      }).join('');\n"
+ "    }\n"
+ "\n"
+ "    function renderPagination(data) {\n"
+ "      var pag = document.getElementById('pagination');\n"
+ "      if (data.totalPages <= 1) { pag.innerHTML = ''; return; }\n"
+ "      pag.innerHTML =\n"
+ "        '<button ' + (data.page <= 1 ? 'disabled' : '') + ' onclick=\"goToPage(1)\">&laquo; First</button>'\n"
+ "        + '<button ' + (data.page <= 1 ? 'disabled' : '') + ' onclick=\"goToPage(' + (data.page - 1) + ')\">&lsaquo; Prev</button>'\n"
+ "        + '<span class=\"page-info\">Page ' + data.page + ' of ' + data.totalPages + '</span>'\n"
+ "        + '<button ' + (data.page >= data.totalPages ? 'disabled' : '') + ' onclick=\"goToPage(' + (data.page + 1) + ')\">Next &rsaquo;</button>'\n"
+ "        + '<button ' + (data.page >= data.totalPages ? 'disabled' : '') + ' onclick=\"goToPage(' + data.totalPages + ')\">Last &raquo;</button>';\n"
+ "    }\n"
+ "\n"
+ "    function goToPage(page) { currentPage = page; loadProducts(); window.scrollTo(0, 0); }\n"
+ "\n"
+ "    // Debounced search\n"
+ "    var searchTimer;\n"
+ "    document.getElementById('searchInput').addEventListener('input', function() {\n"
+ "      clearTimeout(searchTimer);\n"
+ "      searchTimer = setTimeout(function() { currentPage = 1; loadProducts(); }, 300);\n"
+ "    });\n"
+ "\n"
+ "    // Filter & sort change handlers\n"
+ "    document.getElementById('categoryFilter').addEventListener('change', function() { currentPage = 1; loadProducts(); });\n"
+ "    document.getElementById('sortBy').addEventListener('change', function() { currentPage = 1; loadProducts(); });\n"
+ "    document.getElementById('sortOrder').addEventListener('change', function() { currentPage = 1; loadProducts(); });\n"
+ "\n"
+ "    // Initialize\n"
+ "    loadCategories();\n"
+ "    loadProducts();\n"
+ "  </script>\n"
+ "</body>\n"
+ "</html>"
;
    }
}
