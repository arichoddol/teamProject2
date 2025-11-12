package org.spring.backendspring.payment.service.impl;

import lombok.RequiredArgsConstructor;
import org.spring.backendspring.payment.entity.PaymentEntity;
import org.spring.backendspring.payment.repository.PaymentRepository;
import org.spring.backendspring.payment.service.PaymentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Override
    public PaymentEntity createPayment(PaymentEntity payment) {
        PaymentEntity newPayment = PaymentEntity.builder()
                .memberId(payment.getMemberId())
                .paymentAddr(payment.getPaymentAddr())
                .paymentMethod(payment.getPaymentMethod())
                .paymentPost(payment.getPaymentPost())
                .paymentResult(payment.getPaymentResult())
                .paymentType(payment.getPaymentType())
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();

        return paymentRepository.save(newPayment);
    }

    @Override
    public PaymentEntity getPayment(Long paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("결제 정보를 찾을 수 없습니다."));
    }

    @Override
    public List<PaymentEntity> getAllPayments() {
        return paymentRepository.findAll();
    }

    @Override
    public PaymentEntity updatePayment(Long paymentId, PaymentEntity payment) {
        payment.setPaymentId(paymentId);
        payment.setUpdateTime(LocalDateTime.now());
        return paymentRepository.save(payment);
    }

    @Override
    public void deletePayment(Long paymentId) {
        paymentRepository.deleteById(paymentId);
    }
}