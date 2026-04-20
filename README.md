# Retail Platform API

A Spring Boot REST API for a retail domain with customers, products, orders, delivery addresses, and order status management.

## Stack

- Java 17
- Spring Boot 3
- Spring Web
- Spring Data JPA
- Bean Validation
- PostgreSQL
- Maven
- Docker

## Domain

- `Cliente`
- `Producto`
- `Orden`
- `DetalleOrden`
- `DireccionEntrega`
- `EstadoOrden`

## Structure

```text
src
└─ main
   ├─ java/com/jp/testplatformapi
   │  ├─ controller
   │  ├─ dto
   │  │  ├─ request
   │  │  └─ response
   │  ├─ entity
   │  ├─ exception
   │  ├─ mapper
   │  ├─ repository
   │  ├─ service
   │  └─ TestPlatformApiApplication.java
   └─ resources
      └─ application.properties
```

## Endpoints

### Customers

- `POST /clientes`
- `GET /clientes`
- `GET /clientes/{id}`
- `GET /clientes/rut/{rut}`
- `PUT /clientes/{id}`

### Products

- `POST /productos`
- `GET /productos`
- `GET /productos/{id}`
- `GET /productos/sku/{sku}`
- `PUT /productos/{id}`

### Orders

- `POST /ordenes`
- `GET /ordenes`
- `GET /ordenes/{id}`
- `PUT /ordenes/{id}`

## Order Status

- `CREADA`
- `PAGADA`
- `EN_PREPARACION`
- `ENVIADA`
- `ENTREGADA`
- `CANCELADA`

## Run

```bash
mvn spring-boot:run
```
