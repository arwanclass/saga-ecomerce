package com.example.saga.order.integration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;

@Component
public class ProductCatalogClient {
    private final RestClient restClient;

    public ProductCatalogClient(@Value("${app.product-service.base-url}") String productServiceBaseUrl) {
        this.restClient = RestClient.builder().baseUrl(productServiceBaseUrl).build();
    }

    public BigDecimal getProductPrice(String productId) {
        ProductResponse product = restClient.get()
                .uri("/products/{id}", productId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(ProductResponse.class);

        if (product == null || product.price() == null) {
            throw new IllegalArgumentException("Product price not found for productId: " + productId);
        }
        return product.price();
    }

    private record ProductResponse(String id, String name, int stock, BigDecimal price) {}
}
