# Retail Platform API

A small retail backend built with Spring Boot. It is split into separate services so customers, products, and orders can grow on their own while still being easy to run locally.

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
retail-platform-api
├── docker
│   └── postgres
│       └── init-databases.sql
├── services
│   ├── common
│   ├── customer-service
│   ├── product-service
│   └── order-service
├── docker-compose.yml
├── pom.xml
└── README.md
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
