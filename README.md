# GestionStock - Inventory Management Backend

A Spring Boot 3.4.0 REST API built with Java 21 and PostgreSQL to manage products, categories, suppliers, and track automated stock movements. Designed to serve as a high-performance backend for a Flutter mobile application.

---

## 🏗️ Architectural & Structural Overview

The project follows a clean layered architecture to enforce separation of concerns, optimized for security, modularity, and smooth mobile client consumption:

```
src/main/java/com/projet/gestionStock/
├── config/         # Security & Filter setups (JWT Authentication, Web Security)
├── controller/     # REST Controllers exposing the strict /api scope
├── dto/            # Data Transfer Objects
│   ├── request/    # Inbound payload validation structures
│   └── response/   # Outbound response formats (Optimized for Flutter JSON mapping)
├── exception/      # Centralized global exception interception layer
├── model/          # Core JPA Hibernate domain entities
├── repository/     # Data access abstraction layers (Spring Data JPA)
├── service/        # Core business logic processing & transactional isolation
└── specification/  # Dynamic search predicate builders (JPA Criteria API)
```

---

## 🚀 Implemented Core Features

### 1. Secure Authentication Layer

- Safe token-based access utilizing **JWT (JSON Web Tokens)** managed via stateful request interceptors (`JwtAuthFilter`).
- Custom authentication context backed by a unified `CustomUserDetailsService`.
- Dynamic operator extraction directly inside controllers using `@AuthenticationPrincipal`.
- Complete fallback filtering where all security constraints are tightly bounded under the unified `/api/**` scope.

### 2. High-Performance Unified Catalog Engine

- **DRY (Don't Repeat Yourself) Principle Enforcement:** Fully refactored service layers utilizing centralized private mapping handlers (`mapToResponse`) to eliminate compilation redundancy and streamline field modifications.
- Decoupled relationship structures mapping key metadata (`supplierName`, `categoryName`, IDs) instantly, ensuring zero nested serialization loops or deep pointer crashes on orphan records.

### 3. Smart Unified Product Lookup (Best Practice Search)

- Converted dynamic searches into a single, clean endpoint route mapping to the absolute root (`GET /api/products`).
- Leverages optional query string parameters to dynamically apply **JPA Specifications** (`CriteriaAPI`) under the hood, completely mitigating URL path resolution ambiguities.
- Automated low-stock warning triggers via a dedicated proactive view layer predicate:
  $$M_i = \{ p \in P \mid \text{stock}_p \le \text{minStock}_p \}$$

### 4. Dynamic Asset Mutation Tracking

- Core business logic automatically updates overall storage quantities instantly upon incoming stock mutations (`IN`, `OUT`, `ADJUSTMENT`).
- Strict verification checks preventing physical values from dropping sub-zero, throwing a tailored `BadRequestException` whenever bounds are violated.

### 5. Automated Operational Analytics & Dashboard

- **Financial Intelligence:** Aggregates immediate asset value evaluations ($\sum \text{stock} \times \text{price}$) on-the-fly.
- **Flutter Charts Integration:** Tailors specific metric maps safely rounded to 2 decimal places with mathematical zero-division shields for instant rendering in mobile frontends.

---

## 🗺️ Restful API Domain & Architecture Mapping

```
                      ┌──────────────────────────────┐
                      │    Flutter Mobile App Client │
                      └──────────────┬───────────────┘
                                     │
                        HTTP / JSON (RESTful API)
                                     │
                      ┌──────────────▼───────────────┐
                      │    Gateway Base URL: /api    │
                      └──────────────┬───────────────┘
                                     │
         ┌───────────────────────────┼───────────────────────────┐
         ▼                           ▼                           ▼
 ┌───────────────┐           ┌───────────────┐           ┌───────────────┐
 │ Auth Domain   │           │ Operational   │           │ Analytical    │
 │ (Non-Secured) │           │ Core Domains  │           │ Domain        │
 └───────┬───────┘           └───────┬───────┘           └───────┬───────┘
         │                           │                           │
         ├─ /auth/login              ├─ /products                └─ /dashboard
         └─ /auth/register           ├─ /categories
                                     ├─ /suppliers
                                     └─ /movements
```

### 🔑 1. Authentication Naming Scope (`/api/auth`)

- `POST /register` - Registers a new enterprise operator/manager account.
- `POST /login` - Evaluates credentials and passes back the authorization token block.

### 📊 2. Analytical Naming Scope (`/api/dashboard`)

- `GET /` - Resolves aggregated warehouse metrics, daily task volume loops, and pre-calculated statistics.

### 📦 3. Products Operations Naming Scope (`/api/products`)

- `GET /` - Fetches all tracking records **OR** filters them instantly via optional query keys (`name`, `minPrice`, `maxPrice`, `categoryId`).
- `GET /{id}` - Returns the full schema profile of a targeted product block.
- `GET /minstock` - Pulls out critical low inventory anomalies.
- `POST /` - Commits a new inventory tracking object to database storage.
- `PUT /{id}` - Completely updates metadata structures for an existing entity block.
- `DELETE /{id}` - Wipes a specified product block tracking profile securely.

### 📁 4. Categories Naming Scope (`/api/categories`)

- `GET /` - Lists all structural inventory groupings.
- `GET /{id}` - Grabs a single classification record via identifier matching.
- `POST /` - Registers a new taxonomic classification node.
- `PUT /{id}` - Modifies name and description tracking values of a grouping.
- `DELETE /{id}` - Erases a grouping index safely if clear of structural dependencies.

### 🤝 5. Suppliers Naming Scope (`/api/suppliers`)

- `GET /` - Resolves active enterprise partners and wholesale lists.
- `GET /{id}` - Resolves contact cards for a specific individual profile pointer.
- `POST /` - Registers a partner contact point metadata profile.
- `PUT /{id}` - Adjusts detailed address/phone variables within a partner profile.
- `DELETE /{id}` - Drops an existing supplier node indexes securely.

### 🔄 6. Stock Movements Naming Scope (`/api/movements`)

- `POST /` - Generates custom historical transformation sequences (`IN`/`OUT`/`ADJUSTMENT`).
- `GET /` - Audits overall cross-system workflow paths with parameter filters.
- `GET /product/{productId}` - Resolves full historical chronological lifecycle streams for a distinct product.

---

## 🚀 How to Clone & Run the Project Locally

Follow these steps to set up and run the Spring Boot backend on your local machine:

### 1. Prerequisites

Ensure you have the following installed:

- **Java 21** (JDK 21)
- **Maven 3.9+**
- **PostgreSQL** (running locally or via Docker)

### 2. Clone the Repository

Open your terminal and run:

```bash
git clone [https://github.com/YOUR_USERNAME/GestionStock_SpringBoot.git](https://github.com/24156/GestionStock_SpringBoot.git)
cd GestionStock_SpringBoot
```

### 3. Database Configuration

Open src/main/resources/application.properties and update your PostgreSQL credentials:

```bash
spring.datasource.url=jdbc:postgresql://localhost:5432/your_database_name
spring.datasource.username=your_postgres_username
spring.datasource.password=your_postgres_password
spring.jpa.hibernate.ddl-auto=update
```

### 4. Build and Run the Backend

```bash
# Build and download dependencies
mvn clean install

# Run the Spring Boot application
mvn spring-boot:run
```

The server will boot up and listen on http://localhost:8080 under the secure path gateway /api.
