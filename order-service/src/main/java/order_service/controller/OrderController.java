package order_service.controller;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import order_service.client.ProductClient;
import order_service.client.UserClient;
import order_service.entity.Order;
import order_service.repository.OrderRepository;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserClient userClient;

    @Autowired
    private ProductClient productClient;

    @GetMapping
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @PostMapping
    @CircuitBreaker(name = "userService", fallbackMethod = "userServiceFallback")
    public ResponseEntity<?> createOrder(@RequestBody Order order) {
        // 1. Verify User exists (Simulates a call that might fail if user-service is down)
        Object user = userClient.getUserById(order.getUserId());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
        }

        // 2. Verify Product exists (We can wrap this in another method for separate circuit breaker if needed, but for simplicity we call it here)
        Object product = getProductDetails(order.getProductId());
        if (product == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product not found");
        }

        order.setStatus("CREATED");
        Order savedOrder = orderRepository.save(order);
        return ResponseEntity.ok(savedOrder);
    }

    @CircuitBreaker(name = "productService", fallbackMethod = "productServiceFallback")
    private Object getProductDetails(Long productId) {
        return productClient.getProductById(productId);
    }

    // Fallback for User Service
    public ResponseEntity<?> userServiceFallback(Order order, Throwable t) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("User Service is currently down. Fault Tolerance fallback triggered. Order cannot be placed. Reason: " + t.getMessage());
    }

    // Fallback for Product Service
    public Object productServiceFallback(Long productId, Throwable t) {
        // Return a dummy product or null to simulate failure handled gracefully
        throw new RuntimeException("Product Service is currently down. Fault Tolerance fallback triggered. Reason: " + t.getMessage());
    }
}
