package org.spring.backendspring.payment.repository;

import org.spring.backendspring.payment.entity.PaymentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {

    // pgToken 업데이트
    @Modifying
    @Transactional
    @Query("UPDATE PaymentEntity p SET p.pgToken = :pgToken WHERE p.paymentId = :paymentId")
    void updatePgToken(Long paymentId, String pgToken);

    // 결제 성공 여부 업데이트 (isSucceeded 같은 컬럼)
    @Modifying
    @Transactional
    @Query("UPDATE PaymentEntity p SET p.isSucceeded = :status WHERE p.paymentId = :paymentId")
    void updateIsSucced(Long paymentId, int status);

    // PaymentRepository.java
      Page<PaymentEntity> findByPaymentTypeContainingIgnoreCaseOrPaymentPostContainingIgnoreCase(
            String paymentType, String paymentPost, Pageable pageable
    );

}
