package org.spring.backendspring.admin.controller;

import org.spring.backendspring.admin.service.AdminPaymentService;
import org.spring.backendspring.common.dto.PagedResponse;
import org.spring.backendspring.payment.dto.PaymentDto;
import org.spring.backendspring.payment.dto.PaymentItemDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/payments")
public class AdminPaymentController {

    private final AdminPaymentService adminPaymentService;

    @GetMapping
    public ResponseEntity<PagedResponse<PaymentDto>> getPayment(
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {

        // 서비스에서 페이징 처리된 결제 목록을 가져온다
        PagedResponse<PaymentDto> paymentList = adminPaymentService.getAllPayments(keyword, page, size);

        return ResponseEntity.ok(paymentList);
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentItemDto> getPaymentItem(@RequestParam Long paymentId) {
       PaymentItemDto paymentItems = adminPaymentService.getPaymentItemsByPaymentId(paymentId);
       return ResponseEntity.ok(paymentItems);
    }
}
