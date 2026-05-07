package com.example.saga.payment.repository;

import com.example.saga.payment.domain.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<PaymentEntity, String> {
    Optional<PaymentEntity> findByOrderId(String orderId);
}
