package com.example.saga.product.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
public class ProductEntity {
    @Id
    private String id;
    private String name;
    private int stock;
    private BigDecimal price;

    protected ProductEntity() {}

    public ProductEntity(String id, String name, int stock, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.stock = stock;
        this.price = price;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getStock() { return stock; }
    public BigDecimal getPrice() { return price; }

    public void reserve(int quantity) { this.stock -= quantity; }
    public void release(int quantity) { this.stock += quantity; }
}
