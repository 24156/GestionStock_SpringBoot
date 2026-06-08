# GestionStock - Inventory Management Backend

A Spring Boot 3.4.0 REST API built with Java 21 and PostgreSQL to manage products, categories, suppliers, and track automated stock movements. Designed to serve as a reliable backend for a Flutter mobile application.

---

## Architecture & Structural Overview

The project follows a standard layered architecture to enforce a clean separation of concerns:

```
src/main/java/com/projet/gestionStock/
├── config/         # Security and filter configurations (JWT, Web Security)
├── controller/     # REST controllers exposing API endpoints
├── dto/            # Data Transfer Objects
│   ├── request/    # Inbound payload definitions
│   └── response/   # Outbound response formats (Flutter tailored)
├── exception/      # Global handler and application-specific exceptions
├── model/          # JPA Hibernate entities
├── repository/     # Data access abstraction layers
├── service/        # Core business logic processing
└── specification/  # Dynamic search predicate builders (JPA Criteria API)
```

---

## Implemented Core Features

### 1. Secure Authentication Architecture

- Secure token-based access utilizing **JWT (JSON Web Tokens)** managed by `JwtAuthFilter`.
- Custom user authentication configured via Spring Security and a centralized `CustomUserDetailsService`.
- Dynamic extraction of the current authenticated operator inside controllers using `@AuthenticationPrincipal`.

### 2. Supplier & Categorization Engine

- Full structural separation of products using decoupled Category and Supplier entities.
- Data contracts map essential metadata (`supplierName`, `categoryName`, IDs) safely into outbound structures without causing deep nested serialization anomalies or `NullPointerException` bugs on orphan records.

### 3. Inventory Controls & Dynamic Specifications

- Flexible cross-parameter searches using **JPA Specifications** (`CriteriaAPI`) to execute complex dynamic queries on data layers.
- Low stock identification endpoint `/products/minstock` providing proactive tracking for active operations based on the state threshold condition:
  $$M_i = \{ p \in P \mid 	ext{stock}_p \le 	ext{minStock}_p \}$$

### 4. Automated Stock Movements Log

- Dedicated transactional logging mechanism tracing inventory mutations (`IN`, `OUT`, `ADJUSTMENT`).
- Built-in business rules validation: operations falling below zero quantities throw `BadRequestException`.
- Changes trigger an automatic update synchronization routine updating the absolute quantity values within the product records.

---

## Database Schema Model Overview

```
                   ┌──────────────┐
                   │     User     │
                   └──────┬───────┘
                          │ 1
                          │
         ┌────────────────┼────────────────┐
         │ 1              │ 1              │ 1
         ▼                ▼                ▼
   ┌──────────┐     ┌──────────┐     ┌──────────────┐
   │ Category │     │ Supplier │     │StockMovement │
   └────┬─────┘     └────┬─────┘     └──────┬───────┘
        │ 1              │ 1                │ *
        │                │                  │
        └───────┬────────┘                  │
                ▼                           │
          ┌──────────┐                      │
          │ Product  ◄──────────────────────┘
          └──────────┘ *
```

---

## Active API Endpoints Matrix

### Auth Context

- `POST /api/auth/register` - Registers a new manager/user account.
- `POST /api/auth/login` - Validates credentials and generates a JWT.

### Products Context

- `GET /products` - Retrieves all products with full relationship mapping.
- `GET /products/{id}` - Fetches detailed single product records.
- `POST /products` - Commits a new product bound to a specific category and supplier.
- `DELETE /products/{id}` - Removes a product from stock tracking safely.
- `GET /products/minstock` - Returns all items where currently available volume meets low threshold criteria.

### Categories Context

- `GET /categories` - Lists active categories.
- `POST /categories` - Adds a new classification group to the catalog.

### Suppliers Context

- `GET /suppliers` - Retrieves active partners and vendors list.
- `POST /suppliers` - Adds a supplier contact record to the database.

### Stock Movements Context

- `POST /movements` - Commits an inventory adjustment logs (`IN` / `OUT` / `ADJUSTMENT`). Updates core product tables instantly.
- `GET /movements` - Advanced filters over transaction history using criteria keywords.
- `GET /movements/product/{productId}` - Chronological breakdown log tracking a distinct catalog item history.

---

## Future Roadmap Milestones

1. **Dashboard Analytical Aggregations:** Implement isolated data endpoints calculating total capital investments, product volume, and performance distribution matrix statistics.
2. **Export Engineering Utilities:** Integrate system components generating compiled PDF reports and tabular Excel spreadsheet files using clean processing models.
