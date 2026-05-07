package com.example.saga.event;

public record ProductReservationFailedEvent(String orderId, String productId, int quantity, String reason) {
}
