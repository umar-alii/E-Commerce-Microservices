# E-Commerce Microservices Architecture

This project is a complete Spring Boot microservices architecture for an E-Commerce platform. It demonstrates key microservice patterns including centralized configuration, service discovery, API routing, inter-service communication (Feign), distributed tracing (Zipkin), and fault tolerance (Resilience4j).

## 🛠 Environment Requirements

Before running the project, ensure your environment meets the following requirements:

- **Java Version:** JDK 17  
- **Spring Boot Version:** 3.4.3  
- **Spring Cloud Version:** 2024.0.0  
- **Database:** MySQL 8.0 installed locally and running on port `3306`  
  - **Credentials:**  
    - Username: `root`  
    - Password: `root`  
  - *(The databases `user_db`, `product_db`, and `order_db` will be auto-created by Hibernate)*  
- **Docker:** Required for running Zipkin (distributed tracing)  
- **Maven:** For building the project  

## 📁 Architecture Overview

The system consists of the following modules:

1. **Config Server (Port 8888):** Centralized configuration fetched from a Git repository  
2. **Eureka Server (Port 8761):** Service Registry and Discovery  
3. **API Gateway (Port 8080):** Single entry point (built on Netty/WebFlux) routing requests to respective services  
4. **User Service (Port 8081):** Manages user data  
5. **Product Service (Port 8082):** Manages product data  
6. **Order Service (Port 8083):** Manages orders and communicates with User and Product services using OpenFeign, with Resilience4j Circuit Breakers  

## 🔐 Default Credentials

Use the following credentials for secured endpoints:

- **Username:** `admin`  
- **Password:** `admin123`  

## 🚀 How to Run the Application

### Step 1: Push Configurations

The Config Server relies on a remote Git repository.  
Initialize the `config-repo` folder as a Git repository and push it to:
https://github.com/umar-alii/config-repo


*(If this step is skipped, the Config Server will fail to start due to `clone-on-start`.)*

---

### Step 2: Start Zipkin (Distributed Tracing via Docker)

Zipkin is used for distributed tracing across microservices.

Run the following command in the root directory:

```bash
docker-compose up -d
```

This command will:

Pull the Zipkin Docker image (if not already available locally)
Start the Zipkin container in detached mode

Once running, access the Zipkin dashboard at:

http://localhost:9411

### Step 3: Build the Project

From the root directory, build all services using Maven:

mvn clean install -DskipTests
Step 4: Start All Services

Run each microservice application (via IDE or terminal).
All services are configured to work together through Eureka and the Config Server.

You can verify service registration by visiting the Eureka dashboard:

http://localhost:8761

### API Documentation (Swagger UI)

Once the application is running, you can explore and test all APIs via Swagger:

http://localhost:8080/swagger-ui/index.html
