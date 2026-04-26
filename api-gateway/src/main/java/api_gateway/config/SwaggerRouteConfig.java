package api_gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerRouteConfig {

    @Bean
    public RouteLocator swaggerRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user_swagger", r -> r.path("/user-service/v3/api-docs", "/user-service/v3/api-docs/**")
                        .filters(f -> f.rewritePath("/user-service/(?<segment>.*)", "/${segment}"))
                        .uri("lb://user-service"))
                .route("product_swagger", r -> r.path("/product-service/v3/api-docs", "/product-service/v3/api-docs/**")
                        .filters(f -> f.rewritePath("/product-service/(?<segment>.*)", "/${segment}"))
                        .uri("lb://product-service"))
                .route("order_swagger", r -> r.path("/order-service/v3/api-docs", "/order-service/v3/api-docs/**")
                        .filters(f -> f.rewritePath("/order-service/(?<segment>.*)", "/${segment}"))
                        .uri("lb://order-service"))
                .build();
    }
}
