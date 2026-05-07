package com.example.saga.product;

import com.example.saga.product.domain.ProductEntity;
import com.example.saga.product.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.util.List;

@SpringBootApplication
public class ProductServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProductServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner seed(ProductRepository productRepository) {
        return args -> {
            if (productRepository.count() == 0) {
                productRepository.saveAll(List.of(
                        new ProductEntity("SKU-1", "Laptop", 10, new BigDecimal("500")),
                        new ProductEntity("SKU-2", "Monitor", 3, new BigDecimal("300"))
                ));
            }
        };
    }
}
