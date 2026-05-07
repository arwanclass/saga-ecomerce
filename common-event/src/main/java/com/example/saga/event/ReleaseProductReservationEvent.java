package com.example.saga.event;

public record ReleaseProductReservationEvent(String orderId, String productId, int quantity, String reason) {
}
