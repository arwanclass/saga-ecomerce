package com.example.saga.product.kafka;

public final class KafkaTopics {
    private KafkaTopics() {}

    public static final String ORDER_CREATED = "order.created";
    public static final String PRODUCT_RESERVED = "product.reserved";
    public static final String PRODUCT_RESERVATION_FAILED = "product.reservation.failed";
    public static final String RELEASE_PRODUCT_RESERVATION = "product.reservation.release";
}
