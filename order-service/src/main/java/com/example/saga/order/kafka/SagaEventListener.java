package com.example.saga.order.kafka;

import com.example.saga.event.PaymentFailedEvent;
import com.example.saga.event.PaymentProcessedEvent;
import com.example.saga.event.ProductReservationFailedEvent;
import com.example.saga.event.ProductReservedEvent;
import com.example.saga.order.service.OrderService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class SagaEventListener {
    private final OrderService orderService;

    public SagaEventListener(OrderService orderService) {
        this.orderService = orderService;
    }

    @KafkaListener(topics = KafkaTopics.PRODUCT_RESERVED, groupId = "order-service")
    public void onProductReserved(ProductReservedEvent event) {
        orderService.onProductReserved(event.orderId());
    }

    @KafkaListener(topics = KafkaTopics.PRODUCT_RESERVATION_FAILED, groupId = "order-service")
    public void onProductReservationFailed(ProductReservationFailedEvent event) {
        orderService.onProductReservationFailed(event);
    }

    @KafkaListener(topics = KafkaTopics.PAYMENT_PROCESSED, groupId = "order-service")
    public void onPaymentProcessed(PaymentProcessedEvent event) {
        orderService.onPaymentProcessed(event);
    }

    @KafkaListener(topics = KafkaTopics.PAYMENT_FAILED, groupId = "order-service")
    public void onPaymentFailed(PaymentFailedEvent event) {
        orderService.onPaymentFailed(event);
    }
}
