package org.aom.bookstore.orders.clients.catalog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Optional;

@Component
public class ProductServiceClient {

    private static final Logger log = LoggerFactory.getLogger(ProductServiceClient.class);

    private final RestClient restClient;

    public ProductServiceClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public Optional<Product> getProductByCode(String code) {
        log.info("Fetching product for code: {}", code);

        try {
            Product product = restClient.get()
                    .uri("/api/products/{code}", code)
                    .retrieve()
                    .body(Product.class);
            return Optional.ofNullable(product);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Exception while calling catalog-service: {}", e.getCause().getMessage());
            log.error("Exception while calling catalog-service: {}", e.getMessage());
            log.error("Error fetching product with code: {}", code);
            return Optional.empty();
        }
    }
}
