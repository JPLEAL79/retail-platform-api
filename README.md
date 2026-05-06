# Retail Platform API

A small retail backend built with Spring Boot. It is split into separate services so customers, products, and orders can grow on their own while still being easy to run locally.

## Stack

- Java 17
- Spring Boot 3.5.14
- Spring Web
- Spring Data JPA
- Bean Validation
- Spring Boot Actuator
- PostgreSQL
- Maven
- Docker Compose

## Modules

| Module | Port | Database | Responsibility |
| --- | --- | --- | --- |
| `customer-service` | `8082` | `customerdb` | Customer profile and contact data |
| `product-service` | `8083` | `productdb` | Product catalog, price, stock, and active status |
| `order-service` | `8084` | `orderdb` | Orders, order items, delivery address, and status |
| `common` | N/A | N/A | Shared API errors, exceptions, and common properties |

Orders keep a small, explicit lifecycle: `CREATED -> PAID -> DELIVERED`. They can be canceled while they are still `CREATED` or `PAID`; `DELIVERED` and `CANCELED` close the order. Status-only changes use `PATCH /orders/{orderId}/status`.

## Structure

```text
retail-platform-api
|-- docker
|   `-- postgres
|       `-- init-databases.sql
|-- services
|   |-- common
|   |-- customer-service
|   |-- product-service
|   `-- order-service
|-- docker-compose.yml
|-- pom.xml
`-- README.md
```

## Run

Start PostgreSQL:

```bash
docker compose up -d
```

Install the shared module once before running individual services:

```bash
mvn -pl services/common install
```

Run a service:

```bash
mvn -pl services/customer-service spring-boot:run
mvn -pl services/product-service spring-boot:run
mvn -pl services/order-service spring-boot:run
```

Run with a profile:

```bash
mvn -pl services/common install
mvn -pl services/customer-service spring-boot:run -Dspring-boot.run.profiles=local
```

Build all services from the repository root:

```bash
mvn clean package
```

Health checks:

```text
GET http://localhost:8082/actuator/health
GET http://localhost:8083/actuator/health
GET http://localhost:8084/actuator/health
```

Paged list endpoints return `content`, `page`, `size`, `totalElements`, and `totalPages`.

## IDE Notes

The project compiles correctly with Maven on Java 17. If VS Code shows `BOOT_VERSION_VALIDATION_CODE`, that is an informational Spring Boot extension warning about a newer patch version, not a build failure.

For IntelliJ IDEA:

- Open the repository from the root `pom.xml` as a Maven project.
- Use JDK 17 for both the project SDK and Maven importer.
- The warning shown in `docker/postgres/init-databases.sql` about no configured data source is not a SQL error. It only means IntelliJ has no PostgreSQL connection attached to the SQL editor yet.
- Create PostgreSQL data sources with host `localhost`, port `5432`, user `user`, password `pass`, and databases `customerdb`, `productdb`, and `orderdb`.

For VS Code:

- Open the repository root so the Java and Maven extensions detect the full multi-module project.
- Keep Java build configuration reload enabled.
- Use Java 17 to match Maven.
