package com.example.saga.event;

import java.math.BigDecimal;

public record PaymentFailedEvent(String orderId, BigDecimal amount, String reason) {
}
