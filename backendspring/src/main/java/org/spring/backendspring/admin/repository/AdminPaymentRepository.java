package org.spring.backendspring.admin.repository;

import java.util.List;
import java.util.Optional;

import org.spring.backendspring.payment.dto.PaymentItemDto;
import org.spring.backendspring.payment.entity.PaymentEntity;
import org.spring.backendspring.payment.entity.PaymentItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminPaymentRepository extends JpaRepository<PaymentEntity, Long> {
    // search 용도
    Page<PaymentEntity> findByPaymentIdContainingIgnoreCase(
            String paymentIdKeyword,
            Pageable pageable);

    // paymentId로 결제 상품들 조회        
    List<PaymentItemEntity> findPaymentItemsByPaymentId(Long paymentId);
}