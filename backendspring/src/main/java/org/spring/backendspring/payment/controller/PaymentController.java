package org.spring.backendspring.payment.controller;

import lombok.RequiredArgsConstructor;

import org.spring.backendspring.common.dto.PagedResponse;
import org.spring.backendspring.payment.dto.PaymentDto;
import org.spring.backendspring.payment.dto.PaymentItemDto;
import org.spring.backendspring.payment.dto.PaymentResultDto;
import org.spring.backendspring.payment.entity.PaymentEntity;
import org.spring.backendspring.payment.service.PaymentService;
import org.spring.backendspring.payment.service.PaymentResultService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final PaymentResultService paymentResultService;

    // -----------------
    // CRUD 기본 메서드
    // -----------------
    @PostMapping
    public PaymentDto create(@RequestBody PaymentDto paymentDto) {
        return PaymentDto.fromEntity(paymentService.createPayment(paymentDto.toEntity()));
    }

    @GetMapping("/{paymentId}")
    public PaymentDto get(@PathVariable Long paymentId) {
        return PaymentDto.fromEntity(paymentService.getPayment(paymentId));
    }

    @GetMapping
    public List<PaymentDto> getAll() {
        return paymentService.getAllPayments()
                .stream()
                .map(PaymentDto::fromEntity)
                .collect(Collectors.toList());
    }

    @PutMapping("/{paymentId}")
    public PaymentDto update(@PathVariable Long paymentId,
            @RequestBody PaymentDto paymentDto) {
        return PaymentDto.fromEntity(paymentService.updatePayment(paymentId, paymentDto.toEntity()));
    }

    @DeleteMapping("/{paymentId}")
    public String delete(@PathVariable Long paymentId) {
        paymentService.deletePayment(paymentId);
        return "삭제 완료";
    }

    // -----------------
    // Kakao Pay 관련
    // -----------------

    @GetMapping("/approval/{paymentId}/{productPrice}/{memberId}")
    public ResponseEntity<Void> approval(
            @PathVariable Long paymentId,
            @PathVariable Long productPrice,
            @PathVariable Long memberId,
            @RequestParam("pg_token") String pgToken,
            @RequestParam("productName") String productName) {
        try {
            paymentService.paymentApproval(pgToken, paymentId, productPrice, productName, memberId);

            // 성공 시 프론트의 /payment/success로 redirect
            String redirectUrl = "http://localhost:3000/payment/success" +
                    "?paymentId=" + paymentId +
                    "&productPrice=" + productPrice +
                    "&memberId=" + memberId +
                    "&productName=" + URLEncoder.encode(productName, StandardCharsets.UTF_8);

            return ResponseEntity.status(HttpStatus.FOUND)
                    .header("Location", redirectUrl)
                    .build();
        } catch (Exception e) {
            // 실패 시 프론트의 /payment/fail로 redirect
            String failUrl = "http://localhost:3000/payment/fail?paymentId=" + paymentId;
            return ResponseEntity.status(HttpStatus.FOUND)
                    .header("Location", failUrl)
                    .build();
        }
    }

    @PostMapping("/pg/{pg}") // GET 대신 POST로 변경하는 것이 RESTful 설계에 더 적합합니다.
    public Map<String, Object> pgRequest(
            @PathVariable String pg,
            @RequestBody PaymentDto paymentDto) { // PaymentDto 전체를 받음

        // 1. Service 호출: PaymentDto 전체를 넘기도록 수정
        // Service 내부에서 필요한 정보(items, memberId 등)를 추출하여 사용합니다.
        Map<String, Object> map = new HashMap<>();
        
        // ⭐️ [수정된 부분] Service 시그니처에 맞게 PaymentDto 전체를 전달
        String approvalUrl = paymentService.pgRequest(pg, paymentDto); 
        
        map.put("approvalUrl", approvalUrl);
        return map;
    }

    @PostMapping("/fail")
    public Map<String, Object> fail(
            @RequestBody PaymentDto paymentDto,
            @RequestParam("memberId") String memberId) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", "fail 처리 완료");
        return map;
    }

    @GetMapping("/db")
    public Map<String, Object> getDb() {
        Map<String, Object> map = new HashMap<>();
        String dbJsonData = paymentService.getJsonDb();
        map.put("kakaoData", dbJsonData);
        return map;
    }

    @PostMapping("/insert")
    public Map<String, Object> dbInsert(@RequestBody PaymentResultDto dto) {
        Map<String, Object> map = new HashMap<>();
        PaymentResultDto payResult = paymentResultService.dbInsert(dto);
        map.put("payResult", payResult);
        return map;
    }

    @GetMapping("/list")
    public Map<String, Object> getList() {
        Map<String, Object> map = new HashMap<>();
        List<PaymentResultDto> lists = paymentResultService.getList();
        map.put("payRsList", lists);
        return map;
    }

    @GetMapping("/page")
    public PagedResponse<PaymentDto> getPayments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword) {

        // 1. Page<PaymentEntity> 가져오기
        Page<PaymentEntity> pageResult = paymentService.getPayments(page, size, keyword);

        // 2. Entity -> DTO 변환
        Page<PaymentDto> dtoPage = pageResult.map(PaymentDto::fromEntity);

        // 3. PagedResponse로 변환 후 반환
        return PagedResponse.of(dtoPage);
    }


//     회원의 결제 목록을 가져옵니다.
    @GetMapping("/myPayment/{memberId}")
    public ResponseEntity<?> getMemberPaymentList(@PathVariable("memberId") Long memberId,
                                                  @RequestParam(name = "keyword", required = false) String keyword,
                                                  @RequestParam(name = "page", defaultValue = "0") int page,
                                                  @RequestParam(name = "size", defaultValue = "4") int size) {
        PagedResponse<PaymentDto> myPaymentList = paymentService.findMyPaymentList(keyword, memberId, page, size);
        return ResponseEntity.ok(myPaymentList);
    }

}

