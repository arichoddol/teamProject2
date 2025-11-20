package org.spring.backendspring.payment.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.spring.backendspring.payment.dto.KakaoPayPrepareDto;
import org.spring.backendspring.payment.dto.PaymentDto;
import org.spring.backendspring.payment.entity.PaymentEntity;
import org.spring.backendspring.payment.entity.PaymentItemEntity; // ⭐️ 추가
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

    // ... (CRUD 메서드 생략) ...

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

    // ... (paymentApproval, jsonToObject, paymentApproveKakao 메서드 생략) ...
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
            // 주의: dto.getPaymentReadyJson().toString() 대신 dto.getPaymentReadyJson() 사용 권장
            return objectMapper.readValue(dto.getPaymentReadyJson(), PaymentDto.class);
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

    // ⭐️ [수정된 pgRequest] - 다중 아이템 결제를 위해 시그니처 및 로직 변경
    @Override
    public String pgRequest(String pg, Long memberId, List<PaymentItemEntity> itemsToPay) { // ⭐️ 시그니처 변경
        if (!pg.equals("kakao"))
            throw new RuntimeException("제휴되지 않은 결제 업체 입니다.");

        // 1. 총 가격 및 상품명 계산
        long totalAmount = itemsToPay.stream()
                .mapToLong(item -> (long) item.getPrice() * item.getSize())
                .sum();
        String mainItemName = itemsToPay.size() > 1 
                                ? itemsToPay.get(0).getTitle() + " 외 " + (itemsToPay.size() - 1) + "건"
                                : itemsToPay.get(0).getTitle();
        
        // 2. PaymentEntity 생성 및 아이템 연결
        PaymentEntity paymentEntity = new PaymentEntity();
        paymentEntity.setPaymentType("KAKAO");
        paymentEntity.setProductPrice(totalAmount);
        paymentEntity.setMemberId(memberId); // 멤버 ID 설정 추가

        // ⭐️ [핵심 로직 1] PaymentEntity에 PaymentItemEntity 연결
        for (PaymentItemEntity item : itemsToPay) {
            paymentEntity.addPaymentItem(item); // 👈 payment_item_tb 저장을 위한 핵심 호출
        }

        // ⭐️ [핵심 로직 2] PaymentEntity 저장 (Cascade로 PaymentItemEntity도 함께 저장됨)
        // 주의: 이전에 paymentEntity를 저장하고 ID를 가져왔으나, 아이템 연결 후 한 번만 저장하는 것이 깔끔합니다.
        Long paymentId = paymentRepository.save(paymentEntity).getPaymentId();


        // 3. KakaoPay 요청 준비
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "KakaoAK " + "5153d372489b6c481c38dab7bb500441");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("cid", "TC0ONETIME");
        params.add("partner_order_id", String.valueOf(paymentId));
        params.add("partner_user_id", String.valueOf(memberId));
        params.add("item_name", mainItemName); // ⭐️ 대표 상품명 사용
        params.add("quantity", String.valueOf(itemsToPay.size())); // ⭐️ 총 아이템 수량 사용
        params.add("total_amount", String.valueOf(totalAmount)); // ⭐️ 총 결제 금액 사용
        params.add("tax_free_amount", "0");

        String encodedItemName = URLEncoder.encode(mainItemName, StandardCharsets.UTF_8);

        // 백엔드 approval API로 연결
        params.add("approval_url",
                "http://localhost:8088/api/payments/approval/"
                        + paymentId + "/" + totalAmount + "/" + memberId // ⭐️ totalAmount 사용
                        + "?productName=" + encodedItemName); // ⭐️ 대표 상품명 사용

        params.add("cancel_url", "http://localhost:3000/payment/cancel");
        params.add("fail_url", "http://localhost:3000/payment/fail");

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

        // 4. 카카오페이 결제 요청
        ResponseEntity<KakaoPayPrepareDto> result = restTemplate.postForEntity(
                "https://kapi.kakao.com/v1/payment/ready",
                entity,
                KakaoPayPrepareDto.class);

        // 5. 응답 저장
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String kakaoJsonString = objectMapper.writeValueAsString(result.getBody());
            // ⭐️ [핵심 로직 3] PaymentReadyJson 업데이트 후 다시 저장
            paymentEntity.setPaymentReadyJson(kakaoJsonString);
            paymentRepository.save(paymentEntity); // 👈 업데이트 저장
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
            // ⭐️ [개선 권장] N+1 문제 해결을 위해 Repository에 Fetch Join 메서드를 만들어 사용해야 합니다.
            return paymentRepository.findAll(pageable); 
        } else {
            return paymentRepository.findByPaymentTypeContainingIgnoreCaseOrPaymentPostContainingIgnoreCase(
                    keyword, keyword, pageable);
        }
    }
}