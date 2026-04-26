package product_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import product_service.client.UserClient;
import product_service.entity.Product;
import product_service.repository.ProductRepository;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserClient userClient;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public boolean validateProductAndUser(Long productId, Long userId) {
        // 1. Verify Product exists
        Product product = getProductById(productId);
        if (product == null) {
            throw new RuntimeException("Product not found in Product Service");
        }
        
        // 2. Verify User exists via Feign Call
        Object user = userClient.getUserById(userId);
        if (user == null) {
            throw new RuntimeException("User not found in User Service");
        }
        
        return true;
    }
}
