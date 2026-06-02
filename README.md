# RoJira

## Product Catalog Application

A Java-based product catalog web application with a responsive UI and REST API for browsing, searching, filtering, and sorting products.

### Features

- **Product Listing UI** — Responsive, modern product grid with card-based layout
- **Search** — Real-time search across product names and descriptions
- **Category Filtering** — Filter products by category
- **Sorting** — Sort by name, price, or rating (ascending/descending)
- **Pagination** — Server-side pagination for large catalogs
- **REST API** — JSON endpoints for programmatic access
- **Health Check** — Built-in `/health` endpoint for container orchestration

### Project Structure

```
src/
└── main/
    └── java/
        ├── Product.java            # Product model with JSON serialization
        ├── ProductRepository.java  # In-memory product data store
        ├── ProductService.java     # Business logic (filter, sort, search, paginate)
        ├── ProductServer.java      # HTTP server with API handlers and HTML UI
        ├── Calculator.java         # Calculator utility class
        └── Main.java               # Application entry point
```

### How to Run

#### Compile and run locally:
```bash
cd src/main/java
javac -d out Product.java ProductRepository.java ProductService.java ProductServer.java Calculator.java Main.java
cd out
java Main
```

#### Run with Docker:
```bash
docker build -t rojira .
docker run -p 8080:8080 rojira
```

Then open [http://localhost:8080](http://localhost:8080) in your browser.

#### Configure port:
```bash
# Via command-line argument
java Main 3000

# Via environment variable
PORT=3000 java Main
```

### API Endpoints

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/` | GET | Product listing UI page |
| `/products` | GET | Product listing UI page (alias) |
| `/api/products` | GET | List products (JSON) |
| `/api/products/{id}` | GET | Get single product by ID (JSON) |
| `/api/categories` | GET | List all product categories (JSON) |
| `/health` | GET | Health check |

#### Query Parameters for `/api/products`

| Parameter | Default | Description |
|-----------|---------|-------------|
| `search` | — | Search term for name/description |
| `category` | — | Filter by category name |
| `sortBy` | `name` | Sort field: `name`, `price`, `rating` |
| `sortOrder` | `asc` | Sort direction: `asc`, `desc` |
| `page` | `1` | Page number (1-based) |
| `pageSize` | `12` | Items per page |

#### Example API Requests:
```bash
# List all products
curl http://localhost:8080/api/products

# Search for products
curl "http://localhost:8080/api/products?search=bluetooth"

# Filter by category and sort by price
curl "http://localhost:8080/api/products?category=Electronics&sortBy=price&sortOrder=desc"

# Get a single product
curl http://localhost:8080/api/products/P001

# Get all categories
curl http://localhost:8080/api/categories
```

### Calculator Utility

The application also includes a Calculator utility class supporting:
- Addition, Subtraction, Multiplication, Division
- Modulo, Power, Square Root
- Error handling for division by zero and negative square roots