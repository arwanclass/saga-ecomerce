package com.example.saga.event;

public record OrderCancelledEvent(String orderId, String reason) {
}
