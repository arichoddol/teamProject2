package org.spring.backendspring.admin.service;

import org.spring.backendspring.common.dto.PagedResponse;
import org.spring.backendspring.payment.dto.PaymentDto;

public interface AdminPaymentService {

    PagedResponse<PaymentDto> getAllPayments(String keyword, int page, int size);
    
}
