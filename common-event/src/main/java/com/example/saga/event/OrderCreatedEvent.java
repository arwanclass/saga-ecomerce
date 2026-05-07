package com.example.saga.event;

import java.math.BigDecimal;

public record OrderCreatedEvent(String orderId, String productId, int quantity, BigDecimal amount) {
}
