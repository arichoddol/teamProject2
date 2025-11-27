package org.spring.backendspring.payment.controller;

import lombok.RequiredArgsConstructor;
import org.spring.backendspring.payment.entity.PaymentItemEntity;
import org.spring.backendspring.payment.service.PaymentItemService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payment-items")
@RequiredArgsConstructor
public class PaymentItemController {

    private final PaymentItemService itemService;

    // 결제 상품 생성
    @PostMapping
    public PaymentItemEntity create(@RequestBody PaymentItemEntity item) {
        return itemService.createPaymentItem(item);
    }

    // 결제 상품 단일 조회
    @GetMapping("/{id}")
    public PaymentItemEntity get(@PathVariable Long id) {
        return itemService.getPaymentItem(id);
    }

    // 특정 결제(paymentId)의 모든 아이템 조회
    @GetMapping("/by-payment/{paymentId}")
    public List<PaymentItemEntity> getByPayment(@PathVariable Long paymentId) {
        return itemService.getItemsByPaymentId(paymentId);
    }

    // 결제 상품 업데이트
    @PutMapping("/{id}")
    public PaymentItemEntity update(@PathVariable Long id, @RequestBody PaymentItemEntity item) {
        return itemService.updatePaymentItem(id, item);
    }

    // 결제 상품 삭제
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        itemService.deletePaymentItem(id);
    }
}
