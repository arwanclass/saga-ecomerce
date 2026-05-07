package com.example.saga.payment.service;

import com.example.saga.event.PaymentFailedEvent;
import com.example.saga.event.PaymentProcessedEvent;
import com.example.saga.event.ProductReservedEvent;
import com.example.saga.event.RefundPaymentEvent;
import com.example.saga.payment.domain.PaymentEntity;
import com.example.saga.payment.kafka.EventPublisher;
import com.example.saga.payment.kafka.KafkaTopics;
import com.example.saga.payment.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PaymentSagaService {
    private final PaymentRepository paymentRepository;
    private final EventPublisher eventPublisher;

    public PaymentSagaService(PaymentRepository paymentRepository, EventPublisher eventPublisher) {
        this.paymentRepository = paymentRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public void processPayment(ProductReservedEvent event) {
        if (event.amount().compareTo(new BigDecimal("10000")) > 0) {
            eventPublisher.publish(KafkaTopics.PAYMENT_FAILED, event.orderId(),
                    new PaymentFailedEvent(event.orderId(), event.amount(), "Payment rejected by gateway limit"));
            return;
        }

        PaymentEntity payment = paymentRepository.save(PaymentEntity.processed(event.orderId(), event.amount()));
        eventPublisher.publish(KafkaTopics.PAYMENT_PROCESSED, event.orderId(),
                new PaymentProcessedEvent(event.orderId(), event.amount(), payment.getReference()));
    }

    @Transactional
    public void refund(RefundPaymentEvent event) {
        paymentRepository.findByOrderId(event.orderId()).ifPresent(payment -> {
            payment.refund();
            paymentRepository.save(payment);
        });
    }
}
