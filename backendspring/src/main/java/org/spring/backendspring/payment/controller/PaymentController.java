package org.spring.backendspring.payment.controller;

import lombok.RequiredArgsConstructor;
import org.spring.backendspring.payment.entity.PaymentEntity;
import org.spring.backendspring.payment.service.PaymentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    // 결제 생성
    @PostMapping
    public PaymentEntity create(@RequestBody PaymentEntity payment) {
        return paymentService.createPayment(payment);
    }

    // 결제 단일 조회
    @GetMapping("/{id}")
    public PaymentEntity get(@PathVariable Long id) {
        return paymentService.getPayment(id);
    }

    // 모든 결제 조회
    @GetMapping
    public List<PaymentEntity> getAll() {
        return paymentService.getAllPayments();
    }

    // 결제 업데이트
    @PutMapping("/{id}")
    public PaymentEntity update(@PathVariable Long id, @RequestBody PaymentEntity payment) {
        return paymentService.updatePayment(id, payment);
    }

    // 결제 삭제
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        paymentService.deletePayment(id);
    }
}