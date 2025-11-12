package org.spring.backendspring.admin.controller;

import java.util.List;

import org.spring.backendspring.admin.service.AdminMemberService;
import org.spring.backendspring.member.dto.MemberDto;
import org.spring.backendspring.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    private final MemberService memberService;
    private final AdminMemberService adminMemberService;

    @GetMapping({ "", "/", "/adminIndex" })
    // public ResponseEntity<?> adminIndex(@RequestBody MemberDto memberDto) 
    public ResponseEntity<?> adminIndex() 
    {
        // if (memberDto.getRole() == null || !memberDto.getRole().equals("ADMIN")) {
        //     return ResponseEntity.status(403).body("접근 거부: 관리자 권한이 필요합니다.");
        // }
        return ResponseEntity.ok("관리자 페이지에 접근했습니다.");
    }

    @GetMapping("/member/detail/{id}")
    // 특정회원 정보 조회
    public ResponseEntity<MemberDto> getMember(@PathVariable("id") Long id) {
        MemberDto memberDetail = memberService.findById(id);
        return ResponseEntity.ok(memberDetail);
    }

    @GetMapping("/member/memberList")
    // 회원목록 조회
    public ResponseEntity<List<MemberDto>> getAllMembers(){
        List<MemberDto> memberList = adminMemberService.findAllMembers();
        return ResponseEntity.ok(memberList);
     
    }

    @PutMapping("/member/update/{id}")
    // 권한수정, 닉네임수정 정도만
    public ResponseEntity<MemberDto> updateMember(@PathVariable("id") Long id, @RequestBody MemberDto updatedDto) {
            MemberDto updated = memberService.updateMember(id, updatedDto);
            return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/member/delete/{id}")
    // 문제회원 강제탈퇴 등등..
    public ResponseEntity<String> deleleteMember(@PathVariable("id") Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.ok("회원 탈퇴 성공");
    }
}