package order_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service")
public interface ProductClient {

    @GetMapping("/products/{id}")
    Object getProductById(@PathVariable("id") Long id);

    @GetMapping("/products/{productId}/validate-with-user/{userId}")
    Boolean validateProductAndUser(@PathVariable("productId") Long productId, @PathVariable("userId") Long userId);
}
