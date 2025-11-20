package org.spring.backendspring.payment.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import org.spring.backendspring.cart.entity.CartEntity; 
import org.spring.backendspring.cart.entity.CartItemEntity; 
import org.spring.backendspring.cart.repository.CartItemRepository;
import org.spring.backendspring.cart.repository.CartRepository;
import org.spring.backendspring.payment.dto.KakaoPayPrepareDto;
import org.spring.backendspring.payment.dto.PaymentDto;
import org.spring.backendspring.payment.entity.PaymentEntity;
import org.spring.backendspring.payment.entity.PaymentItemEntity;
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

    // Lombok @RequiredArgsConstructor가 필드들을 자동 주입합니다.
    private final PaymentRepository paymentRepository;
    private final PaymentResultRepository paymentResultRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    // --- CRUD 메서드 ---

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

    // --- KakaoPay 관련 메서드 ---

    @Override
    @Transactional // ⭐️ 트랜잭션 보장 (결제 승인과 장바구니 삭제를 하나의 트랜잭션으로)
    public void paymentApproval(String pgToken, Long paymentId, Long productPrice, String productName, Long memberId) {
        paymentRepository.updatePgToken(paymentId, pgToken);
        PaymentEntity paymentEntity = paymentRepository.findById(paymentId).orElseThrow();
        PaymentDto paymentDto = PaymentDto.toDto(paymentEntity);

        PaymentDto getTidPaymentDto = jsonToObject(paymentDto);
        paymentDto.setTid(getTidPaymentDto.getTid());

        if (pgToken == null)
            throw new RuntimeException("pgToken이 존재하지 않습니다.");
        
        // 1. 카카오페이 최종 승인 요청 및 isSucceeded 업데이트
        paymentApproveKakao(paymentDto, paymentId, productPrice, productName, memberId);

        // 2. 결제 성공 확인 후 장바구니 아이템 삭제
        PaymentEntity approvedPayment = paymentRepository.findById(paymentId).orElseThrow();
        
        // paymentApproveKakao에서 isSucceeded가 1로 업데이트되었다고 가정합니다.
        if (approvedPayment.getIsSucceeded() == 1) { 
            removePaidItemsFromCart(approvedPayment);
        }
    }

    private PaymentDto jsonToObject(PaymentDto dto) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
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
        headers.set("Authorization", "KakaoAK " + "5153d372489b6c481c38dab7bb500441"); // 🔑 인증키

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
            paymentRepository.updateIsSucced(paymentId, 1); // 성공 처리
        } else {
            paymentRepository.updateIsSucced(paymentId, 0); // 실패 처리
        }
    }
    
    // ⭐️ [신규 메서드] 결제된 상품을 장바구니에서 삭제하는 로직
    private void removePaidItemsFromCart(PaymentEntity paymentEntity) {
        
        Long memberId = paymentEntity.getMemberId();
        
        // 1. 결제된 상품 ID 목록 추출 (PaymentEntity는 Lazy Loading이므로 트랜잭션 내에서 호출 필요)
        List<Long> paidItemIds = paymentEntity.getPaymentItemEntities().stream()
            .map(PaymentItemEntity::getItemId) 
            .collect(Collectors.toList());
        
        // 2. 해당 회원의 장바구니 찾기
        CartEntity cartEntity = cartRepository.findByMemberId(memberId).orElse(null); 

        if (cartEntity != null && !paidItemIds.isEmpty()) {
            // 3. ⭐️ 수정된 Repository 메서드 이름 사용
            List<CartItemEntity> itemsToDelete = cartItemRepository.findByCartEntityAndItemEntity_IdIn(
                cartEntity, paidItemIds
            );
            
            if (!itemsToDelete.isEmpty()) {
                cartItemRepository.deleteAll(itemsToDelete);
                System.out.println("장바구니에서 결제 완료된 상품 " + itemsToDelete.size() + "개를 삭제했습니다.");
            }
        }
    }

    // --- PG 요청 (결제 준비) 메서드 ---

    @Override
    public String pgRequest(String pg, Long memberId, List<PaymentItemEntity> itemsToPay) {
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
        paymentEntity.setMemberId(memberId);

        for (PaymentItemEntity item : itemsToPay) {
            paymentEntity.addPaymentItem(item); 
        }

        Long paymentId = paymentRepository.save(paymentEntity).getPaymentId();


        // 3. KakaoPay 요청 준비
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "KakaoAK " + "5153d372489b6c481c38dab7bb500441"); // 🔑 인증키

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("cid", "TC0ONETIME");
        params.add("partner_order_id", String.valueOf(paymentId));
        params.add("partner_user_id", String.valueOf(memberId));
        params.add("item_name", mainItemName);
        params.add("quantity", String.valueOf(itemsToPay.size()));
        params.add("total_amount", String.valueOf(totalAmount));
        params.add("tax_free_amount", "0");

        String encodedItemName = URLEncoder.encode(mainItemName, StandardCharsets.UTF_8);

        // 백엔드 approval API로 연결
        params.add("approval_url",
                "http://localhost:8088/api/payments/approval/"
                        + paymentId + "/" + totalAmount + "/" + memberId
                        + "?productName=" + encodedItemName);

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
            paymentEntity.setPaymentReadyJson(kakaoJsonString);
            paymentRepository.save(paymentEntity);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("카카오 결제 요청 변환 오류", e);
        }

        return result.getBody().getNext_redirect_pc_url();
    }

    // --- 기타 메서드 ---

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