package org.spring.backendspring.payment.repository;

import java.util.List;
import java.util.Optional;

import org.spring.backendspring.payment.entity.PaymentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {

    @Query("SELECT p FROM PaymentEntity p JOIN FETCH p.paymentItemEntities WHERE p.paymentId = :paymentId")
    Optional<PaymentEntity> findByIdWithItems(@Param("paymentId") Long paymentId);

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

    // ⭐️ [추가] Payment 목록 조회 시 PaymentItemEntity를 EAGER 로딩 (N+1 해결)
    @Query(value = "SELECT p FROM PaymentEntity p JOIN FETCH p.paymentItemEntities", countQuery = "SELECT COUNT(p) FROM PaymentEntity p")
    Page<PaymentEntity> findAllWithItems(Pageable pageable);

    // ⭐️ [추가] 검색어 포함 시 PaymentItemEntity를 EAGER 로딩 (N+1 해결)
    @Query(value = "SELECT p FROM PaymentEntity p JOIN FETCH p.paymentItemEntities WHERE LOWER(p.paymentType) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(p.paymentPost) LIKE LOWER(CONCAT('%', :keyword, '%'))", countQuery = "SELECT COUNT(p) FROM PaymentEntity p WHERE LOWER(p.paymentType) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(p.paymentPost) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<PaymentEntity> findByKeywordWithItems(@Param("keyword") String keyword, Pageable pageable);

    Page<PaymentEntity> findByMemberId(Pageable pageable, Long memberId);

    @Query(value = "SELECT p FROM PaymentEntity p JOIN FETCH p.paymentItemEntities item " +
            "WHERE p.memberId = :memberId AND item.title LIKE %:keyword%",
            countQuery = "SELECT COUNT(DISTINCT p) FROM PaymentEntity p JOIN p.paymentItemEntities item " +
                    "WHERE p.memberId = :memberId AND item.title LIKE %:keyword%")
    Page<PaymentEntity> findByMemberIdAndTitleContaining(Pageable pageable, String keyword, Long memberId);

    // 기존 메서드는 삭제하거나 주석 처리하고, 위 메서드를 사용하도록 서비스 레이어에서 변경됩니다.
    // Page<PaymentEntity>
    // findByPaymentTypeContainingIgnoreCaseOrPaymentPostContainingIgnoreCase(
    //     String paymentType, String paymentPost, Pageable pageable);
}