package com.example.saga.payment.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "payments")
public class PaymentEntity {
    @Id
    private String id;
    private String orderId;
    private BigDecimal amount;
    private String reference;
    private PaymentStatus status;

    protected PaymentEntity() {}

    public static PaymentEntity processed(String orderId, BigDecimal amount) {
        PaymentEntity payment = new PaymentEntity();
        payment.id = UUID.randomUUID().toString();
        payment.orderId = orderId;
        payment.amount = amount;
        payment.reference = "PAY-" + UUID.randomUUID().toString().substring(0, 8);
        payment.status = PaymentStatus.PROCESSED;
        return payment;
    }

    public String getId() { return id; }
    public String getOrderId() { return orderId; }
    public BigDecimal getAmount() { return amount; }
    public String getReference() { return reference; }
    public PaymentStatus getStatus() { return status; }

    public void refund() { this.status = PaymentStatus.REFUNDED; }
}
