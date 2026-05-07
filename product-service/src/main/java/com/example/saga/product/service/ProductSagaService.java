package com.example.saga.product.service;

import com.example.saga.event.OrderCreatedEvent;
import com.example.saga.event.ProductReservationFailedEvent;
import com.example.saga.event.ProductReservedEvent;
import com.example.saga.event.ReleaseProductReservationEvent;
import com.example.saga.product.domain.ProductEntity;
import com.example.saga.product.kafka.EventPublisher;
import com.example.saga.product.kafka.KafkaTopics;
import com.example.saga.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class ProductSagaService {
    private final ProductRepository productRepository;
    private final EventPublisher eventPublisher;

    public ProductSagaService(ProductRepository productRepository, EventPublisher eventPublisher) {
        this.productRepository = productRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public void reserveProduct(OrderCreatedEvent event) {
        ProductEntity product = productRepository.findById(event.productId()).orElse(null);
        if (product == null) {
            eventPublisher.publish(KafkaTopics.PRODUCT_RESERVATION_FAILED, event.orderId(),
                    new ProductReservationFailedEvent(event.orderId(), event.productId(), event.quantity(),
                            "Product not found"));
            return;
        }

        if (product.getStock() < event.quantity()) {
            eventPublisher.publish(KafkaTopics.PRODUCT_RESERVATION_FAILED, event.orderId(),
                    new ProductReservationFailedEvent(event.orderId(), event.productId(), event.quantity(),
                            "Insufficient stock"));
            return;
        }

        product.reserve(event.quantity());
        productRepository.save(product);

        eventPublisher.publish(KafkaTopics.PRODUCT_RESERVED, event.orderId(),
                new ProductReservedEvent(event.orderId(), event.productId(), event.quantity(), event.amount()));
    }

    @Transactional
    public void releaseReservation(ReleaseProductReservationEvent event) {
        ProductEntity product = productRepository.findById(event.productId()).orElse(null);
        if (product == null) return;
        product.release(event.quantity());
        productRepository.save(product);
    }
}
