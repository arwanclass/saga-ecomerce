package com.example.saga.payment.controller;

import com.example.saga.payment.domain.PaymentEntity;
import com.example.saga.payment.repository.PaymentRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    private final PaymentRepository paymentRepository;

    public PaymentController(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @GetMapping
    public List<PaymentEntity> all() {
        return paymentRepository.findAll();
    }

    @GetMapping("/order/{orderId}")
    public PaymentEntity byOrder(@PathVariable String orderId) {
        return paymentRepository.findByOrderId(orderId).orElseThrow();
    }
}
