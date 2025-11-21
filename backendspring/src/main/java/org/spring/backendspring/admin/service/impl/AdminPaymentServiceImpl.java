package org.spring.backendspring.admin.service.impl;

import org.spring.backendspring.admin.repository.AdminPaymentRepository;
import org.spring.backendspring.admin.service.AdminPaymentService;
import org.spring.backendspring.common.dto.PagedResponse;
import org.spring.backendspring.payment.dto.PaymentDto;
import org.spring.backendspring.payment.dto.PaymentItemDto;
import org.spring.backendspring.payment.entity.PaymentItemEntity;
import org.spring.backendspring.payment.repository.PaymentItemRepository;
import org.spring.backendspring.payment.repository.PaymentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminPaymentServiceImpl implements AdminPaymentService {

    private final AdminPaymentRepository adminPaymentRepository;
    private final PaymentItemRepository paymentItemRepository;

    @Override
    public PagedResponse<PaymentDto> getAllPayments(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("paymentId").descending());
        Page<PaymentDto> paymentPage;

        if (keyword == null || keyword.trim().isEmpty()) {
            paymentPage = adminPaymentRepository.findAll(pageable)
                    .map(entity -> PaymentDto.fromEntity(entity));
        } else {
            paymentPage = adminPaymentRepository
                    .findByPaymentIdContainingIgnoreCase(keyword, pageable)
                    .map(entity -> PaymentDto.fromEntity(entity));
        }
        return PagedResponse.of(paymentPage);
    }

    @Override
    public List<PaymentItemDto> getPaymentItemsByPaymentId(Long paymentId) {
        List<PaymentItemEntity> items = paymentItemRepository.findByPayment_PaymentId(paymentId);

        return items.stream()
                .map(PaymentItemDto::fromEntity)
                .toList();
    }

    @Override
    public PaymentDto getPayment(Long paymentId) {
        return adminPaymentRepository.findById(paymentId)
                .map(PaymentDto::fromEntity)
                .orElseThrow(() -> new RuntimeException("결제 정보 없음"));
    }



}

