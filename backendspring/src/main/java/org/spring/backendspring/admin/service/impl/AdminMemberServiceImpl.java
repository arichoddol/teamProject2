
package org.spring.backendspring.admin.service.impl;

import java.util.List;

import org.spring.backendspring.admin.service.AdminMemberService;

import org.spring.backendspring.member.MemberMapper;
import org.spring.backendspring.member.dto.MemberDto;
import org.spring.backendspring.member.entity.MemberEntity;
import org.spring.backendspring.member.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminMemberServiceImpl implements AdminMemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    // 멤버 전체 조회
    @Override
    public List<MemberDto> findAllMembers() {
        List<MemberEntity> memberList = memberRepository.findAll();
        return memberList.stream()
                .map(MemberMapper::toDto)
                .toList();
    }
}
