package com.example.saga.payment.kafka;

public final class KafkaTopics {
    private KafkaTopics() {}

    public static final String PRODUCT_RESERVED = "product.reserved";
    public static final String PAYMENT_PROCESSED = "payment.processed";
    public static final String PAYMENT_FAILED = "payment.failed";
    public static final String REFUND_PAYMENT = "payment.refund";
}
