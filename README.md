# Retail Platform API

Spring Boot retail platform split into independent services for customers, products, and orders.

## Stack

- Java 17
- Spring Boot 3.5.13
- Spring Web
- Spring Data JPA
- Bean Validation
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

## Structure

```text
docker/
  postgres/
    init-databases.sql
services/
  common/
  customer-service/
  product-service/
  order-service/
docker-compose.yml
pom.xml
```

## Run

Start PostgreSQL:

```bash
docker compose up -d
```

Run one service:

```bash
cd services/customer-service
mvn spring-boot:run
```

Build all services from the repository root:

```bash
mvn clean package
```
