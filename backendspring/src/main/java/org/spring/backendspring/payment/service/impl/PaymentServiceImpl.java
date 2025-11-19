package org.spring.backendspring.payment.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.spring.backendspring.payment.dto.KakaoPayPrepareDto;
import org.spring.backendspring.payment.dto.PaymentDto;
import org.spring.backendspring.payment.entity.PaymentEntity;
import org.spring.backendspring.payment.repository.PaymentRepository;
import org.spring.backendspring.payment.repository.PaymentResultRepository;
import org.spring.backendspring.payment.service.PaymentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentResultRepository paymentResultRepository;

    @Override
    public PaymentEntity createPayment(PaymentEntity payment) {
        return paymentRepository.save(
                PaymentEntity.builder()
                        .memberId(payment.getMemberId())
                        .paymentAddr(payment.getPaymentAddr())
                        .paymentMethod(payment.getPaymentMethod())
                        .paymentPost(payment.getPaymentPost())
                        .paymentResult(payment.getPaymentResult())
                        .paymentType(payment.getPaymentType())
                        .paymentStatus(payment.getPaymentStatus())
                        .build());
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
        PaymentEntity existing = getPayment(paymentId);
        existing.setPaymentAddr(payment.getPaymentAddr());
        existing.setPaymentMethod(payment.getPaymentMethod());
        existing.setPaymentPost(payment.getPaymentPost());
        existing.setPaymentResult(payment.getPaymentResult());
        existing.setPaymentType(payment.getPaymentType());
        existing.setPaymentStatus(payment.getPaymentStatus());
        return paymentRepository.save(existing);
    }

    @Override
    public void deletePayment(Long paymentId) {
        paymentRepository.deleteById(paymentId);
    }

    // ----------------- KakaoPay -----------------
    @Override
    public void paymentApproval(String pgToken, Long paymentId, Long productPrice, String productName, Long memberId) {
        paymentRepository.updatePgToken(paymentId, pgToken);
        PaymentEntity paymentEntity = paymentRepository.findById(paymentId).orElseThrow();
        PaymentDto paymentDto = PaymentDto.toDto(paymentEntity);

        PaymentDto getTidPaymentDto = jsonToObject(paymentDto);
        paymentDto.setTid(getTidPaymentDto.getTid());

        if (pgToken == null)
            throw new RuntimeException("pgToken이 존재하지 않습니다.");
        paymentApproveKakao(paymentDto, paymentId, productPrice, productName, memberId);
    }

    private PaymentDto jsonToObject(PaymentDto dto) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            return objectMapper.readValue(dto.getPaymentReadyJson().toString(), PaymentDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void paymentApproveKakao(PaymentDto paymentDto, Long paymentId, Long productPrice, String productName,
            Long memberId) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "KakaoAK " + "5153d372489b6c481c38dab7bb500441");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("cid", "TC0ONETIME");
        params.add("tid", paymentDto.getTid());
        params.add("partner_order_id", String.valueOf(paymentId));
        params.add("partner_user_id", String.valueOf(memberId));
        params.add("pg_token", paymentDto.getPgToken());
        params.add("total_amount", String.valueOf(productPrice));

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

        ResponseEntity<String> result = restTemplate.postForEntity(
                "https://kapi.kakao.com/v1/payment/approve",
                entity,
                String.class);

        System.out.println("결제 승인 응답: " + result.getBody());

        if (result.getStatusCode() == HttpStatus.OK) {
            paymentRepository.updateIsSucced(paymentId, 1);
        } else {
            paymentRepository.updateIsSucced(paymentId, 0);
        }
    }

    @Override
    public String pgRequest(String pg, Long productId, Long memberId, Long productPrice, String productName) {
        if (!pg.equals("kakao"))
            throw new RuntimeException("제휴되지 않은 결제 업체 입니다.");

        PaymentEntity paymentEntity = new PaymentEntity();
        paymentEntity.setPaymentType("KAKAO");
        paymentEntity.setProductPrice(productPrice);
        Long paymentId = paymentRepository.save(paymentEntity).getPaymentId();

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "KakaoAK " + "5153d372489b6c481c38dab7bb500441");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("cid", "TC0ONETIME");
        params.add("partner_order_id", String.valueOf(paymentId));
        params.add("partner_user_id", String.valueOf(memberId));
        params.add("item_name", productName);
        params.add("quantity", "1");
        params.add("total_amount", String.valueOf(productPrice));
        params.add("tax_free_amount", "0");

        String encodedProductName = URLEncoder.encode(productName, StandardCharsets.UTF_8);

        // 백엔드 approval API로 연결
        params.add("approval_url",
                "http://localhost:8088/api/payments/approval/"
                        + paymentId + "/" + productPrice + "/" + memberId
                        + "?productName=" + encodedProductName);

        params.add("cancel_url", "http://localhost:3000/payment/cancel");
        params.add("fail_url", "http://localhost:3000/payment/fail");

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

        ResponseEntity<KakaoPayPrepareDto> result = restTemplate.postForEntity(
                "https://kapi.kakao.com/v1/payment/ready",
                entity,
                KakaoPayPrepareDto.class);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String kakaoJsonString = objectMapper.writeValueAsString(result.getBody());
            paymentEntity.setPaymentReadyJson(kakaoJsonString);
            paymentRepository.save(paymentEntity);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("카카오 결제 요청 변환 오류", e);
        }

        return result.getBody().getNext_redirect_pc_url();
    }

    @Override
    public String getJsonDb() {
        List<PaymentEntity> list = paymentRepository.findAll();
        List<PaymentDto> jsonDb = list.stream()
                .map(el -> PaymentDto.builder()
                        .paymentReadyJson(el.getPaymentReadyJson())
                        .build())
                .collect(Collectors.toList());

        return "" + jsonDb;
    }

    // PaymentServiceImpl.java
    @Override
    public Page<PaymentEntity> getPayments(int page, int size, String keyword) {
        Pageable pageable = PageRequest.of(page, size);
        if (keyword == null || keyword.isEmpty()) {
            return paymentRepository.findAll(pageable);
        } else {
            return paymentRepository.findByPaymentTypeContainingIgnoreCaseOrPaymentPostContainingIgnoreCase(
                    keyword, keyword, pageable);
        }
    }

}
