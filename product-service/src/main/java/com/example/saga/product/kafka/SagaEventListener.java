package com.example.saga.product.kafka;

import com.example.saga.event.OrderCreatedEvent;
import com.example.saga.event.ReleaseProductReservationEvent;
import com.example.saga.product.service.ProductSagaService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class SagaEventListener {
    private final ProductSagaService productSagaService;

    public SagaEventListener(ProductSagaService productSagaService) {
        this.productSagaService = productSagaService;
    }

    @KafkaListener(topics = KafkaTopics.ORDER_CREATED, groupId = "product-service")
    public void onOrderCreated(OrderCreatedEvent event) {
        productSagaService.reserveProduct(event);
    }

    @KafkaListener(topics = KafkaTopics.RELEASE_PRODUCT_RESERVATION, groupId = "product-service")
    public void onReleaseReservation(ReleaseProductReservationEvent event) {
        productSagaService.releaseReservation(event);
    }
}
