package org.spring.backendspring.admin.service;

import org.spring.backendspring.common.dto.PagedResponse;
import org.spring.backendspring.payment.dto.PaymentDto;
import org.spring.backendspring.payment.dto.PaymentItemDto;

public interface AdminPaymentService {

    PagedResponse<PaymentDto> getAllPayments(String keyword, int page, int size);

    PaymentItemDto getPaymentItemsByPaymentId(Long paymentId);

    
    
}
