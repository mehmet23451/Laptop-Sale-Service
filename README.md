# Laptop Sale Service

A containerized Spring Boot RESTful API for managing laptop sales transactions, automated CRUD entities, and dynamic foreign exchange pricing.

## Tech Stack & Dependencies

- Java 17 (Eclipse Temurin JRE)
- Spring Boot 3
- Spring Security & JWT (JSON Web Tokens)
- Spring Data JPA & Hibernate
- Spring Data REST
- PostgreSQL 15 (Alpine)
- Docker & Docker Compose
- JUnit 5 & Mockito (Unit Testing)
- OpenAPI 3.1 / Swagger UI

## Features

- JWT Authentication: User registration, authentication, and session refreshment via refresh tokens.
- Transactional Laptop Sales: Atomic purchase handling (@Transactional) with buyer/seller balance transfers and inventory status updates.
- Dynamic Currency Conversion: Real-time USD-to-TRY exchange rate calculation via Central Bank of Turkey (TCMB EVDS) API.
- Weekend Safeguard: Calendar check preventing external exchange rate queries during market closures (Saturday & Sunday).
- Automated Entity CRUD: Spring Data REST endpoints exposing standard CRUD, sorting, and pagination for domain models.
- Comprehensive Unit Tests: Mockito-driven test suite verifying business logic, balance checks, seller ownership validations, and transactional rollbacks.
- Containerized Setup: Multi-container orchestration using Docker Compose with PostgreSQL health checks.

## Project Structure

```text
src/
├── main/java/com/example/demo/
│   ├── config/             # Spring Security, AuthenticationProvider, and core configs
│   ├── controller/         # REST endpoints for custom business flows
│   ├── dto/                # Data Transfer Objects and payload schemas
│   ├── entity/             # JPA entities (Laptop, Customer, Seller, Account, etc.)
│   ├── exception/          # Custom exceptions and GlobalExceptionHandler
│   ├── jwt/                # JWT generation, extraction, and request filters
│   ├── repo/               # Spring Data JPA repositories
│   └── service/            # Core business rules and service contracts
└── test/java/com/example/demo/
    └── service/            # Mockito unit tests for service business logic
```

## API Endpoints

### Authentication
- POST `/register` - Register a new user
- POST `/authenticate` - Authenticate credentials and retrieve JWT token
- POST `/refreshToken` - Refresh expired access token

### Business Operations
- POST `/sellLaptop` - Execute a laptop sale transaction

### Entity CRUD (Spring Data REST)
Standard CRUD endpoints with built-in pagination and sorting:
- `/laptops`
- `/customers`
- `/sellers`
- `/accounts`
- `/addresses`

## Testing

Run unit tests via Maven:
```bash
mvn clean test
```

## Getting Started

### Prerequisites

- JDK 17+
- Maven 3.8+
- Docker & Docker Compose

### 1. Configuration
Set the TCMB API key in `src/main/resources/application.properties` or provide it as an environment variable:
```properties
tcmb.api.key=${TCMB_API_KEY}
```

### 2. Run with Docker Compose
Build the JAR and start both PostgreSQL and the Spring application:
```bash
mvn clean package -DskipTests
docker-compose up --build -d
```

Containers started:
- `postgres-db`: PostgreSQL 15 on port `5433` (internal: `5432`)
- `demo-container`: Spring Boot application on port `8080`

### 3. Run Locally (Without Docker)
Ensure PostgreSQL is running locally on port `5432`, then:
```bash
mvn spring-boot:run
```

## API Documentation

Swagger UI is available once the application is running:  
http://localhost:8080/swagger-ui/index.html

## License

This project is licensed under the MIT License.
