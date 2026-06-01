# Smart Inventory (Gestion de Stock) — Backend REST API

An academic, secure, and production-grade RESTful API designed as a decoupled backend system for inventory management. Built with **Spring Boot 3.4** and **Spring Security**, this architecture focuses strictly on the clean isolation of data presentation layers, stateless cryptographic validation, and uniform exception propagation. It is fully engineered to be consumed by a minimalist, pixel-perfect frontend client like **Flutter**.

---

## 🏗️ Core Architectural Design

The backend implements an **N-Tier Layered Architecture** coupled with structural design patterns to preserve data integrity, loose coupling, and strict compliance with the _Single Responsibility Principle_.

```
[HTTP Request] ──> [Controller Layer] ──> [Service Layer] ──> [Repository Layer] ──> [Database (H2)]
                         │                    │
                (Validates DTOs)     (Handles Business Logic)
```

### 1. Architectural Layers

- **Presentation Layer (`Controllers`)**: Intercepts HTTP incoming requests, handles syntax mapping, handles payload validation constraints, and routes requests to the business layer.
- **Business Logic Layer (`Services`)**: Orchestrates data calculations, processes internal validation checks, enforces business isolation rules, and evaluates contextual token security identity.
- **Data Access Layer (`Repositories`)**: Inherits from `JpaRepository` abstraction interfaces to handle transparent ORM-based transaction management with the persistent engine.

### 2. Data Transfer Object (DTO) Design Pattern

To prevent security leaks (such as exposing password crypt-hashes) and mitigate deep endless serialization cycles common in cyclic database relationships, the system systematically decouples database `@Entity` definitions from public APIs:

- **Incoming Requests (`*Request`)**: Contain only primitive properties necessary for raw resource instantiation (e.g., bypassing full object references by utilizing a simple `Long categoryId`).
- **Outgoing Responses (`*Response`)**: Provide a flattened, optimal, and performance-tuned payload structure structured cleanly for rapid UI styling, rendering contextual foreign labels (e.g., embedding a flat `categoryName` text field rather than loading parent tables entirely).

---

## 🔒 Advanced Security & Identity Propagation

The security layout utilizes an asynchronous, stateless defensive framework managed through **Spring Security 6**.

```
[Protected Endpoint] ──> [JwtAuthFilter] ──> Validates Token signature ──> Injects SecurityContext
```

### 1. Stateless Authentication Pipeline (`JWT`)

Every endpoint context under the root except `/auth/**` and `/h2-console/**` requires strong authorization. A custom filter context (`JwtAuthFilter`) intercepts arriving transactions to parse the incoming headers:

- It reads the string following the `Bearer ` statement within the `Authorization` request header.
- It decrypts and verifies the cryptographic token key signature to ensure the packet has not been modified or expired.
- Upon successful validation, it resolves the username _Subject_ and mounts the authentication properties securely inside the thread-local **`SecurityContextHolder`**.

### 2. Dynamic Principal Binding (`@AuthenticationPrincipal`)

To prevent unauthorized user cross-contamination or parameter hijacking (where a client manually forces or targets resources belonging to another user id), the server manages ownership dynamically. Controllers utilize the `@AuthenticationPrincipal` annotation to transparently receive the runtime session owner identity from the token payload, binding category and product models automatically to the authenticating context.

---

## 🛠️ Global Exception Handling & Error Architecture

A dedicated centralized handler annotated with `@ControllerAdvice` hooks directly into the framework execution tree to guarantee predictable error JSON response signatures for external application clients.

```
                  ┌───> Catches [ResourceNotFoundException] ───> Returns 404 Not Found JSON
[@ControllerAdvice]
                  └───> Catches [AuthenticationException]   ───> Returns 401 Unauthorized JSON
```

### 1. Business Logic Exception Catching

When a query parameter maps to a non-existent index key or a resource missing from database tables, the core raises a unique `ResourceNotFoundException`. The interceptor immediately absorbs this event, formatting a clean payload mapping to an accurate **`404 Not Found`** HTTP status code.

### 2. Customized Authentication Error Mapping

By default, internal credential filter failures result in generic server execution bubbles returning blank headers with unformatted statuses. To address this, a dedicated **`CustomAuthenticationEntryPoint`** intercepts unauthorized tokens at the root level, writing a clean, structured JSON format containing uniform property structures matching the standard application metadata:

```json
{
  "status": 401,
  "message": "Unauthorized: Token is missing, invalid or expired.",
  "timestamp": "2026-06-01T21:45:12.395Z"
}
```

---

## 📑 Feature Breakdown & Endpoints Matrix

| Module                    | Context Route    | HTTP Verb | Authentication Requirement | Payload Contract & Behavior                                                                                                                                 | Expected HTTP Status |
| :------------------------ | :--------------- | :-------: | :------------------------: | :---------------------------------------------------------------------------------------------------------------------------------------------------------- | :------------------: |
| **Authentication**        | `/auth/register` |  `POST`   |           `None`           | Receives sign-up credentials. Persists new user profiles while securely omitting the hashed password password from the response model.                      |   **201 Created**    |
| **Authentication**        | `/auth/login`    |  `POST`   |           `None`           | Validates active credentials against store hashes. Issues a secure signed JWT string token upon validation.                                                 |      **200 OK**      |
| **Categories Management** | `/categories`    |  `POST`   |       `Bearer Token`       | Accepts classification names. Inherently ties the object mapping profile to the calling contextual user ID.                                                 |   **201 Created**    |
| **Categories Management** | `/categories`    |   `GET`   |       `Bearer Token`       | Resolves a structured array list containing inventory tracking models initialized by the caller.                                                            |      **200 OK**      |
| **Products Management**   | `/products`      |  `POST`   |       `Bearer Token`       | Stores dynamic items with constraint keys (stock metrics, pricing models, parent category link). Generates response mappings including flat category names. |   **201 Created**    |
| **Products Management**   | `/products`      |   `GET`   |       `Bearer Token`       | Fetches an optimized list representation displaying products mapped to the signed-in profile scope.                                                         |      **200 OK**      |

---

## 🚀 Environment Requirements & Execution Instructions

### Prerequisites

- **Java Development Kit (JDK)**: Version 17 or higher (Java 21 recommended).
- **Build System**: Apache Maven (Wrapper binary embedded).
- **Database**: In-Memory H2 Engine (Pre-configured for local testing environments).

### Compilation & Application Startup

To clean target structures, assemble source assets, compile packages, and spin up the Tomcat network socket on local port `8081`, run the following lifecycle commands within your Linux terminal workspace:

```bash
# Clean project build files and boot the Spring application instance
./mvnw clean spring-boot:run
```

The service console will broadcast initializing logs, and the database workspace console will become viewable locally via the interface route: `http://localhost:8081/h2-console`.

---

_Developed as an engineering blueprint for secure decoupled application development._
