package org.spring.backendspring.admin.repository;

import org.spring.backendspring.payment.entity.PaymentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminPaymentRepository extends JpaRepository<PaymentEntity, Long> {
    Page<PaymentEntity> findByPaymentIdContainingIgnoreCase(
        String paymentIdKeyword,
        Pageable pageable
    );
}