package order_service.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import order_service.client.ProductClient;
import order_service.entity.Order;
import order_service.repository.OrderRepository;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductClient productClient;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order createOrder(Order order) {
        // Validation: Order -> Product -> User
        Boolean isValid = validateOrderDetails(order.getProductId(), order.getUserId());
        if (isValid == null || !isValid) {
            throw new RuntimeException("Validation failed for Product or User.");
        }

        order.setStatus("CREATED");
        return orderRepository.save(order);
    }

    @CircuitBreaker(name = "productService", fallbackMethod = "productServiceFallback")
    private Boolean validateOrderDetails(Long productId, Long userId) {
        return productClient.validateProductAndUser(productId, userId);
    }

    // Fallback for Product Service (and implicitly User Service since it's chained)
    public Boolean productServiceFallback(Long productId, Long userId, Throwable t) {
        throw new RuntimeException("Product Service (or User Service) is currently down. Fault Tolerance fallback triggered. Reason: " + t.getMessage());
    }
}
