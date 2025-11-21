package org.spring.backendspring.payment.service;

import org.spring.backendspring.common.dto.PagedResponse;
import org.spring.backendspring.payment.dto.PaymentDto;
import org.spring.backendspring.payment.entity.PaymentEntity;
import org.spring.backendspring.payment.entity.PaymentItemEntity; // ⭐️ 임포트 추가
import org.springframework.data.domain.Page;

import java.util.List;

public interface PaymentService {
    PaymentEntity createPayment(PaymentEntity payment);

    PaymentEntity getPayment(Long paymentId);

    List<PaymentEntity> getAllPayments();

    PaymentEntity updatePayment(Long paymentId, PaymentEntity payment);

    void deletePayment(Long paymentId);

    void paymentApproval(String pgToken, Long paymentId, Long productPrice, String productName, Long memberId);

    // ⭐️ [수정된 부분] 단일 상품 정보 대신 List<PaymentItemEntity>를 받도록 변경
    String pgRequest(String pg, Long memberId, List<PaymentItemEntity> itemsToPay);

    String getJsonDb();

    // PaymentService.java
    Page<PaymentEntity> getPayments(int page, int size, String keyword);

    PagedResponse<PaymentDto> findMyPaymentList(Long memberId,  int page, int size);
}