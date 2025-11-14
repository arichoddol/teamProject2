package org.spring.backendspring.payment.controller;

import lombok.RequiredArgsConstructor;
import org.spring.backendspring.payment.dto.PaymentDto;
import org.spring.backendspring.payment.service.PaymentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    // 결제 생성
    @PostMapping
    public PaymentDto create(@RequestBody PaymentDto paymentDto) {
        return PaymentDto.fromEntity(paymentService.createPayment(paymentDto.toEntity()));
    }

    // 결제 단일 조회
    @GetMapping("/{paymentId}")
    public PaymentDto get(@PathVariable Long paymentId) {
        return PaymentDto.fromEntity(paymentService.getPayment(paymentId));
    }

    // 모든 결제 조회
    @GetMapping
    public List<PaymentDto> getAll() {
        return paymentService.getAllPayments()
                .stream()
                .map(PaymentDto::fromEntity)
                .collect(Collectors.toList());
    }

    // 결제 업데이트
    @PutMapping("/{paymentId}")
    public PaymentDto update(@PathVariable Long paymentId,
                             @RequestBody PaymentDto paymentDto) {
        return PaymentDto.fromEntity(paymentService.updatePayment(paymentId, paymentDto.toEntity()));
    }

    // 결제 삭제
    @DeleteMapping("/{paymentId}")
    public String delete(@PathVariable Long paymentId) {
        paymentService.deletePayment(paymentId);
        return "삭제 완료";
    }
}
