package com.example.saga.product.controller;

import com.example.saga.product.domain.ProductEntity;
import com.example.saga.product.repository.ProductRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping
    public List<ProductEntity> all() {
        return productRepository.findAll();
    }

    @GetMapping("/{id}")
    public ProductEntity byId(@PathVariable("id") String id) {
        return productRepository.findById(id).orElseThrow();
    }
}
