package com.example.saga.order.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "orders")
public class OrderEntity {
    @Id
    private String id;
    private String productId;
    private int quantity;
    private BigDecimal amount;
    private OrderStatus status;
    private boolean productReserved;
    private boolean paymentProcessed;
    private boolean forceFailureAfterPayment;

    public static OrderEntity create(String productId, int quantity, BigDecimal amount) {
        OrderEntity order = new OrderEntity();
        order.id = UUID.randomUUID().toString();
        order.productId = productId;
        order.quantity = quantity;
        order.amount = amount;
        order.status = OrderStatus.PENDING;
        return order;
    }

    public String getId() { return id; }
    public String getProductId() { return productId; }
    public int getQuantity() { return quantity; }
    public BigDecimal getAmount() { return amount; }
    public OrderStatus getStatus() { return status; }
    public boolean isProductReserved() { return productReserved; }
    public boolean isPaymentProcessed() { return paymentProcessed; }
    public boolean isForceFailureAfterPayment() { return forceFailureAfterPayment; }

    public void markProductReserved() { this.productReserved = true; }
    public void markPaymentProcessed() { this.paymentProcessed = true; }
    public void complete() { this.status = OrderStatus.COMPLETED; }
    public void cancel() { this.status = OrderStatus.CANCELLED; }
    public void enableForceFailureAfterPayment() { this.forceFailureAfterPayment = true; }
}
