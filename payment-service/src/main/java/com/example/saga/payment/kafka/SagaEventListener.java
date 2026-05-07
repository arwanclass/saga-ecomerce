package com.example.saga.payment.kafka;

import com.example.saga.event.ProductReservedEvent;
import com.example.saga.event.RefundPaymentEvent;
import com.example.saga.payment.service.PaymentSagaService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class SagaEventListener {
    private final PaymentSagaService paymentSagaService;

    public SagaEventListener(PaymentSagaService paymentSagaService) {
        this.paymentSagaService = paymentSagaService;
    }

    @KafkaListener(topics = KafkaTopics.PRODUCT_RESERVED, groupId = "payment-service")
    public void onProductReserved(ProductReservedEvent event) {
        paymentSagaService.processPayment(event);
    }

    @KafkaListener(topics = KafkaTopics.REFUND_PAYMENT, groupId = "payment-service")
    public void onRefundPayment(RefundPaymentEvent event) {
        paymentSagaService.refund(event);
    }
}
