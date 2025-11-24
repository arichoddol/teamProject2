package org.spring.backendspring.member.controller;

import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.spring.backendspring.config.security.MyUserDetails;
import org.spring.backendspring.member.dto.MemberDto;
import org.spring.backendspring.member.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/join")
    public ResponseEntity<?> join(@Valid @RequestPart("memberDto") MemberDto memberDto,
                                  BindingResult bindingResult,
                                  @RequestPart(value = "memberFile", required = false) MultipartFile memberFile) {
        try {
            if (bindingResult.hasErrors()) {
                Map<String, Object> errors = new HashMap<>();
                bindingResult.getFieldErrors().forEach(err -> {
                    errors.put(err.getField(), err.getDefaultMessage());
                });
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
            }
            memberService.insertMember(memberDto, memberFile);
            return ResponseEntity.ok("회원가입 성공");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원가입 실패: " + e.getMessage());
        }
    }

    @GetMapping("/detail/{id}")
    @PreAuthorize("isAuthenticated() and #id.toString() == authentication.principal.memberEntity.id.toString()")
    public ResponseEntity<?> getMember(@PathVariable("id") Long id,
                                       @AuthenticationPrincipal MyUserDetails myUserDetails) {
        MemberDto memberDetail = memberService.findById(id);
        return ResponseEntity.ok(memberDetail);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("isAuthenticated() and #id.toString() == authentication.principal.memberEntity.id.toString()")
    public ResponseEntity<?> updateMember(@PathVariable("id") Long id,
                                          @RequestPart("memberDto") MemberDto memberDto,
                                          @RequestPart(value = "memberFile", required = false) MultipartFile memberFile,
                                          @AuthenticationPrincipal MyUserDetails myUserDetails) {
        MemberDto updated = memberService.updateMember(id, memberDto, memberFile);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteMember(@PathVariable Long id,
                                               @AuthenticationPrincipal MyUserDetails myUserDetails) {
        if (!id.equals(myUserDetails.getMemberId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("삭제 권한이 없습니다: 로그인 정보가 일치하지 않습니다.");
        }
        memberService.deleteMember(id);
        return ResponseEntity.ok("회원 탈퇴 성공");
    }
}
