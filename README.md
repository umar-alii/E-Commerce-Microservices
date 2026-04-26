# E-Commerce Microservices Architecture

This project is a complete Spring Boot microservices architecture for an E-Commerce platform. It demonstrates key microservice patterns including centralized configuration, service discovery, API routing, inter-service communication (Feign), distributed tracing (Zipkin), and fault tolerance (Resilience4j).

## 🛠 Environment Requirements

Before running the project, ensure your environment meets the following requirements:

- **Java Version:** JDK 17
- **Spring Boot Version:** 3.4.3
- **Spring Cloud Version:** 2024.0.0
- **Database:** MySQL 8.0 installed locally and running on port `3306`.
  - **Credentials:** Username: `root`, Password: `root`
  - *(The databases `user_db`, `product_db`, and `order_db` will be auto-created by Hibernate)*
- **Docker:** Required for running the Zipkin tracing dashboard.
- **Maven:** For building the project.

## 📁 Architecture Overview

The system consists of the following modules:
1. **Config Server (Port 8888):** Centralized configuration fetched from a Git repository.
2. **Eureka Server (Port 8761):** Service Registry and Discovery.
3. **API Gateway (Port 8080):** Single entry point (built on Netty/WebFlux) routing requests to respective services.
4. **User Service (Port 8081):** Manages user data.
5. **Product Service (Port 8082):** Manages product data.
6. **Order Service (Port 8083):** Manages orders. Communicates with User and Product services using OpenFeign and implements Resilience4j Circuit Breakers for fault tolerance.

## 🚀 How to Run the Application

Because of inter-dependencies (e.g., services needing the Config Server, and the Gateway needing Eureka), **you must start the services in a specific order**.

### Step 1: Push Configurations
The Config Server relies on a remote Git repository. 
You must initialize the `config-repo` folder as a Git repository and push it to:
`https://github.com/umar-alii/config-repo`
*(If you do not do this, the Config Server will fail to start on `clone-on-start`)*

### Step 2: Start Zipkin (Tracing)
Open a terminal in the root directory and run:
```bash
docker-compose up -d
```
*(You can access the Zipkin dashboard at http://localhost:9411)*

### Step 3: Compile the Project
From the root directory, build all microservices using the universal parent POM:
```bash
mvn clean install -DskipTests
```

### Step 4: Start the Services (Strict Order)
Run the main application class for each service in the exact order below:

1. **Start `config-server`** (Wait for it to fully initialize and listen on port 8888)
2. **Start `eureka-server`** (Wait for it to listen on port 8761)
3. **Start `api-gateway`** (Wait for it to register with Eureka)
4. **Start `user-service`, `product-service`, and `order-service`** (Can be started concurrently)

*Tip: Check the Eureka Dashboard at `http://localhost:8761` to verify all services have successfully registered before making API calls.*
