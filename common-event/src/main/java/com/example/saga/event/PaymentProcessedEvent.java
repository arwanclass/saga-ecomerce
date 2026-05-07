package com.example.saga.event;

import java.math.BigDecimal;

public record PaymentProcessedEvent(String orderId, BigDecimal amount, String paymentReference) {
}
