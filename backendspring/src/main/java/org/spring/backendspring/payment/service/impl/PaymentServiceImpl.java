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
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentResultRepository paymentResultRepository;

    // -----------------
    // CRUD 메서드
    // -----------------
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

    // -----------------
    // KakaoPay 관련
    // -----------------
    @Override
    public void paymentApproval(String pgToken, Long paymentId, Long productPrice, String productName, Long memberId) {
        paymentRepository.updatePgToken(paymentId, pgToken);
        PaymentEntity paymentEntity = paymentRepository.findById(paymentId).orElseThrow();
        PaymentDto paymentDto = PaymentDto.toDto(paymentEntity);

        PaymentDto getTidPaymentDto = jsonToObject(paymentDto);
        paymentDto.setTid(getTidPaymentDto.getTid());

        paymentApproveKakao(paymentDto, paymentId, productPrice, productName, memberId);
    }

    public PaymentDto jsonToObject(PaymentDto dto) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            return objectMapper.readValue(dto.getPaymentReadyJson().toString(), PaymentDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void paymentApproveKakao(PaymentDto paymentDto, Long paymentId, Long productPrice, String productName, Long memberId) {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();
        String kakaoJsonString;

        Optional<PaymentEntity> optionalPaymentEntity = paymentRepository.findById(paymentDto.getPaymentId());
        if (optionalPaymentEntity.isPresent()) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "783f67cfd35797dcfb3c369ebd13d398");
            HttpEntity<String> entity = new HttpEntity<>(headers);

            URI uri = UriComponentsBuilder
                    .fromUriString("https://kapi.kakao.com")
                    .path("/v1/payment/approve")
                    .queryParam("cid", "TC0ONETIME")
                    .queryParam("partner_order_id", paymentDto.getPaymentId())
                    .queryParam("partner_user_id", memberId)
                    .queryParam("item_name", productName)
                    .queryParam("tid", paymentDto.getTid())
                    .queryParam("total_amount", productPrice)
                    .queryParam("tax_free_amount", "100")
                    .queryParam("pg_token", paymentDto.getPgToken())
                    .encode()
                    .build()
                    .toUri();

            ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);

            try {
                kakaoJsonString = objectMapper.writeValueAsString(result.getBody());
                System.out.println(kakaoJsonString);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("kakao payment request : json to string error : " + e);
            }

            paymentRepository.updateIsSucced(paymentDto.getPaymentId(), 1);

        } else {
            throw new IllegalArgumentException("해당 아이디에 값이 없습니다.");
        }
    }

    @Override
    public String pgRequest(String pg, Long productId, Long memberId, Long productPrice, String productName) {
        if (!pg.equals("kakao")) throw new RuntimeException("제휴되지 않은 결제 업체 입니다.!!!!");

        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();
        PaymentEntity paymentEntity = new PaymentEntity();
        String kakaoJsonString;
        Long paymentId;

        paymentEntity.setPaymentType("KAKAO");
        paymentEntity.setProductPrice(productPrice);
        paymentId = paymentRepository.save(paymentEntity).getPaymentId();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "KakaoAK 카카오키");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        URI uri = UriComponentsBuilder
                .fromUriString("https://kapi.kakao.com")
                .path("/v1/payment/ready")
                .queryParam("cid", "TC0ONETIME")
                .queryParam("partner_order_id", paymentId)
                .queryParam("partner_user_id", memberId)
                .queryParam("item_name", productName)
                .queryParam("quantity", "1")
                .queryParam("total_amount", productPrice)
                .queryParam("tax_free_amount", "100")
                .queryParam("approval_url", "http://localhost:3000/react/payment/approval/"
                        + paymentId + "/" + productPrice + "/" + productName + "/" + memberId)
                .queryParam("cancel_url", "http://localhost:8088/payment/cancel")
                .queryParam("fail_url", "http://localhost:8088/payment/fail")
                .encode()
                .build()
                .toUri();

        ResponseEntity<KakaoPayPrepareDto> result = restTemplate.exchange(uri, HttpMethod.POST, entity, KakaoPayPrepareDto.class);

        try {
            kakaoJsonString = objectMapper.writeValueAsString(result.getBody());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("kakao payment request : json to string error : " + e);
        }

        paymentEntity.setPaymentReadyJson(kakaoJsonString);
        paymentRepository.save(paymentEntity);
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

}
