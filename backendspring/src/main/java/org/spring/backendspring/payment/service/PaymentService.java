package org.spring.backendspring.payment.service;

import org.spring.backendspring.payment.entity.PaymentEntity;

import java.util.List;

public interface PaymentService {
    PaymentEntity createPayment(PaymentEntity payment);
    PaymentEntity getPayment(Long paymentId);
    List<PaymentEntity> getAllPayments();
    PaymentEntity updatePayment(Long paymentId, PaymentEntity payment);
    void deletePayment(Long paymentId);
}
