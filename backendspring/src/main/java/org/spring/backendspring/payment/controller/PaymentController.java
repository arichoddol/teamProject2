package org.spring.backendspring.payment.controller;

import lombok.RequiredArgsConstructor;
import org.spring.backendspring.payment.dto.PaymentDto;
import org.spring.backendspring.payment.dto.PaymentResultDto;
import org.spring.backendspring.payment.service.PaymentService;
import org.spring.backendspring.payment.service.PaymentResultService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final PaymentResultService paymentResultService; // 대문자로 수정

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

    @GetMapping("/approval/{paymentId}/{productPrice}/{productName}/{memberId}")
    public String approval(
            @PathVariable Long paymentId,
            @PathVariable Long productPrice,
            @PathVariable String productName,
            @PathVariable Long memberId,
            @RequestParam("pg_token") String pgToken
    ) {
        paymentService.paymentApproval(pgToken, paymentId, productPrice, productName, memberId);
        return "OK";
    }

    @GetMapping("/pg/{pg}")
    public Map<String, Object> pgRequest(
            @PathVariable String pg,
            @RequestParam Long productId,
            @RequestParam Long memberId,
            @RequestParam Long productPrice,
            @RequestParam String productName
    ) {
        Map<String, Object> map = new HashMap<>();
        String approvalUrl = paymentService.pgRequest(pg, productId, memberId, productPrice, productName);
        map.put("approvalUrl", approvalUrl);
        return map;
    }

    @PostMapping("/fail")
    public Map<String, Object> fail(
            @RequestBody PaymentDto paymentDto,
            @RequestParam("memberId") String memberId
    ) {
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
}
