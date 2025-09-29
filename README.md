# Franchise Microservice

##  Overview
This microservice manages **franchises**, their **branches**, and the **inventory** of products associated with those branches.

- A **Franchise** has an `id` and `name`, and contains multiple branches.
- A **Branch** belongs to a franchise and has an `id`, `name`, and a list of inventories.
- **Inventory** is a many-to-many relationship between **Branch** and **Product** (a product can belong to multiple branches, and a branch can have multiple products).

The service is developed with **Spring Boot + WebFlux**, reactive programming with **Project Reactor**, and exposes REST endpoints for CRUD operations.

---

## ⚙️ Technologies
- Java 21
- Spring Boot 3+
- Spring WebFlux
- R2DBC
- Reactor
- Gradle
- Docker
- OpenAPI/Swagger (for API documentation)

---

## 🔧 Environment Variables

The service requires the following environment variables to connect to the database:

| Variable                     | Description                  | Example    |
|-------------------------------|------------------------------|------------|
| `SPRING_DATASOURCE_HOST`      | Database host                | `localhost` |
| `SPRING_DATASOURCE_USERNAME`  | Database username            | `postgres` |
| `SPRING_DATASOURCE_PASSWORD`  | Database password            | `admin` |


You can configure them in your IDE (Run/Debug Configurations) or export them in your terminal:

```
export SPRING_DATASOURCE_HOST=localhost
export SPRING_DATASOURCE_USERNAME=postgres
export SPRING_DATASOURCE_PASSWORD=admin
```

## ️ Running Locally

1. **Clone the repository**
   ```bash
   git clone https://github.com/your-org/franchise-microservice.git
   cd franchise-microservice


2. **Run the service**
```
   ./gradlew clean bootRun
```

# --- Franchise ---
# Create franchise
Request
```
curl --location 'localhost:8080/api/franchise' \
--header 'Content-Type: application/json' \
--data '{"name":"COCA COLA - branch"}'
```
Response
```JSON
{
"success": true,
"message": "Successful operation",
"data": {
    "id": 28,
    "franchiseId": 1,
    "name": "COCA COLA - branch"
},
"timestamp": "2025-09-29T07:08:42.0190428"
}
```

# Update franchise name
Request
```
curl --location --request PUT 'localhost:8081/api/franchise' \
--header 'Content-Type: application/json' \
--data '{
   "franchiseId":1,
   "name":"BBC"
}
```

Response
```JSON
{
   "success": true,
   "message": "Successful operation",
   "data": {
      "id": 1,
      "name": "BBC"
   },
   "timestamp": "2025-09-29T07:14:12.9503015"
}
```

# --- Branch ---

# Create branch
Request
```
curl --location 'localhost:8080/api/branch' \
--header 'Content-Type: application/json' \
--data '{"franchiseId":233,"name":"COCA COLA - branch"}'
```
Response

```JSON
{
   "success": true,
   "message": "Successful operation",
   "data": {
      "id": 28,
      "franchiseId": 1,
      "name": "COCA COLA - branch"
   },
   "timestamp": "2025-09-29T07:08:42.0190428"
}
```

# Update branch name
Request
```
curl --location --request PUT 'localhost:8081/api/franchise' \
--header 'Content-Type: application/json' \
--data '{
"franchiseId":1,
"name":"BBC"

}
```

Response

```JSON
{
   "success": true,
   "message": "Successful operation",
   "data": {
      "id": 1,
      "name": "BBC"
   },
   "timestamp": "2025-09-29T07:14:12.9503015"
}
```

# --- Product ---
# Create product

Request

```
curl --location 'localhost:8080/api/product' \
--header 'Content-Type: application/json' \
--data '{"name":"COCA COLA"}'
```
Response
```JSON
{
    "success": true,
    "message": "Successful operation",
    "data": {
        "id": 25,
        "name": "COCA COLA"
    },
    "timestamp": "2025-09-29T07:09:28.4869371"
}
```

# Update product name

Request
```
curl --location --request PUT 'localhost:8080/api/product' \
--header 'Content-Type: application/json' \
--data '{"productId":25,"name":"COCA COLA 3"}'
```

Response
```JSON
{
    "success": true,
    "message": "Successful operation",
    "data": {
        "id": 25,
        "name": "COCA COLA 3"
    },
    "timestamp": "2025-09-29T07:17:30.7158432"
}
```

# --- Inventory ---


# Create inventory (associate product with branch)

Request

```
curl --location 'localhost:8080/api/inventory' \
--header 'Content-Type: application/json' \
--data '{"branchId":1,"productId":25}'
```

Response
```JSON
{
"success": true,
"message": "Successful operation",
"data": {
    "id": 8,
    "branchId": 1,
    "productId": 25,
    "stock": 0
},
"timestamp": "2025-09-29T07:09:35.3079413"
}
```


# Delete inventory relation (remove product from branch)

Request
```
curl --location --request DELETE 'localhost:8080/api/inventory/branch/1/product/25'
```

# Update stock

Request
```
curl --location --request PATCH 'localhost:8080/api/inventory' \
--header 'Content-Type: application/json' \
--data '{"branchId":2,"productId":3,"stock":27}'
```
Response

```JSON
{
    "success": true,
    "message": "Successful operation",
    "data": {
        "id": 3,
        "branchId": 2,
        "productId": 3,
        "stock": 27
    },
    "timestamp": "2025-09-29T07:12:36.7091738"
}
```


# Get top products by stock for a franchise

Request
```
curl --location 'localhost:8080/api/inventory/top-products/1'
```

Response
```JSON
{
"success": true,
"message": "Successful operation",
"data": [
{
   "branchId": 1,
   "branchName": "Sucursal Norte",
   "productId": 1,
   "productName": "Coca-Cola",
   "stock": 50
},
{
   "branchId": 2,
   "branchName": "BBC sucursal",
   "productId": 3,
   "productName": "Agua",
   "stock": 27
}
],
"timestamp": "2025-09-29T08:29:12.9461147"
}
```