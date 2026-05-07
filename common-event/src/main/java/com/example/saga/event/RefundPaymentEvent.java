package com.example.saga.event;

import java.math.BigDecimal;

public record RefundPaymentEvent(String orderId, BigDecimal amount, String reason) {
}
