package org.spring.backendspring.member.controller;

import lombok.RequiredArgsConstructor;
import org.spring.backendspring.member.dto.MemberDto;
import org.spring.backendspring.member.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody MemberDto memberDto) {
        try {
            memberService.insertMember(memberDto);
            return ResponseEntity.ok("회원가입 성공");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원가입 실패: " + e.getMessage());
        }
    }

    @GetMapping("/detail/{id}")
    @PreAuthorize("#id == authentication.principal.memberEntity.id")
    public ResponseEntity<?> getMember(@PathVariable("id") Long id) {
        MemberDto memberDetail = memberService.findById(id);
        return ResponseEntity.ok(memberDetail);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("#id == authentication.principal.memberEntity.id")
    public ResponseEntity<?> updateMember(@PathVariable("id") Long id,
                                          @RequestBody MemberDto updatedDto) {
        MemberDto updated = memberService.updateMember(id, updatedDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("#id == authentication.principal.memberEntity.id")
    public ResponseEntity<String> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.ok("회원 탈퇴 성공");
    }
}
