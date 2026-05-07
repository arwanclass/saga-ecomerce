package com.example.saga.order.service;

import com.example.saga.event.OrderCancelledEvent;
import com.example.saga.event.OrderCompletedEvent;
import com.example.saga.event.OrderCreatedEvent;
import com.example.saga.event.PaymentFailedEvent;
import com.example.saga.event.PaymentProcessedEvent;
import com.example.saga.event.ProductReservationFailedEvent;
import com.example.saga.event.RefundPaymentEvent;
import com.example.saga.event.ReleaseProductReservationEvent;
import com.example.saga.order.domain.OrderEntity;
import com.example.saga.order.domain.OrderStatus;
import com.example.saga.order.integration.ProductCatalogClient;
import com.example.saga.order.kafka.EventPublisher;
import com.example.saga.order.kafka.KafkaTopics;
import com.example.saga.order.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final EventPublisher eventPublisher;
    private final ProductCatalogClient productCatalogClient;

    public OrderService(OrderRepository orderRepository, EventPublisher eventPublisher,
                        ProductCatalogClient productCatalogClient) {
        this.orderRepository = orderRepository;
        this.eventPublisher = eventPublisher;
        this.productCatalogClient = productCatalogClient;
    }

    @Transactional
    public OrderEntity createOrder(String productId, int quantity) {
        BigDecimal amount = productCatalogClient.getProductPrice(productId).multiply(BigDecimal.valueOf(quantity));
        OrderEntity order = orderRepository.save(OrderEntity.create(productId, quantity, amount));
        eventPublisher.publish(KafkaTopics.ORDER_CREATED, order.getId(),
                new OrderCreatedEvent(order.getId(), order.getProductId(), order.getQuantity(), order.getAmount()));
        return order;
    }

    @Transactional
    public OrderEntity enableCompensationDemo(String orderId) {
        OrderEntity order = get(orderId);
        order.enableForceFailureAfterPayment();
        return orderRepository.save(order);
    }

    @Transactional
    public void onProductReserved(String orderId) {
        OrderEntity order = get(orderId);
        if (order.getStatus() != OrderStatus.PENDING) return;
        order.markProductReserved();
        orderRepository.save(order);
    }

    @Transactional
    public void onPaymentProcessed(PaymentProcessedEvent event) {
        OrderEntity order = get(event.orderId());
        if (order.getStatus() != OrderStatus.PENDING) return;

        order.markPaymentProcessed();

        if (order.isForceFailureAfterPayment()) {
            order.cancel();
            orderRepository.save(order);
            eventPublisher.publish(KafkaTopics.ORDER_CANCELLED, order.getId(),
                    new OrderCancelledEvent(order.getId(), "Forced failure after payment for compensation demo"));
            eventPublisher.publish(KafkaTopics.RELEASE_PRODUCT_RESERVATION, order.getId(),
                    new ReleaseProductReservationEvent(order.getId(), order.getProductId(), order.getQuantity(),
                            "Rollback stock because order is cancelled after payment"));
            eventPublisher.publish(KafkaTopics.REFUND_PAYMENT, order.getId(),
                    new RefundPaymentEvent(order.getId(), order.getAmount(),
                            "Refund because order is cancelled after payment"));
            return;
        }

        order.complete();
        orderRepository.save(order);
        eventPublisher.publish(KafkaTopics.ORDER_COMPLETED, order.getId(), new OrderCompletedEvent(order.getId()));
    }

    @Transactional
    public void onProductReservationFailed(ProductReservationFailedEvent event) {
        OrderEntity order = get(event.orderId());
        if (order.getStatus() != OrderStatus.PENDING) return;

        order.cancel();
        orderRepository.save(order);
        eventPublisher.publish(KafkaTopics.ORDER_CANCELLED, order.getId(),
                new OrderCancelledEvent(order.getId(), event.reason()));
    }

    @Transactional
    public void onPaymentFailed(PaymentFailedEvent event) {
        OrderEntity order = get(event.orderId());
        if (order.getStatus() != OrderStatus.PENDING) return;

        order.cancel();
        orderRepository.save(order);
        eventPublisher.publish(KafkaTopics.ORDER_CANCELLED, order.getId(),
                new OrderCancelledEvent(order.getId(), event.reason()));

        if (order.isProductReserved()) {
            eventPublisher.publish(KafkaTopics.RELEASE_PRODUCT_RESERVATION, order.getId(),
                    new ReleaseProductReservationEvent(order.getId(), order.getProductId(), order.getQuantity(),
                            "Rollback stock because payment failed"));
        }
    }

    public OrderEntity get(String orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));
    }

    public List<OrderEntity> list() {
        return orderRepository.findAll();
    }
}
