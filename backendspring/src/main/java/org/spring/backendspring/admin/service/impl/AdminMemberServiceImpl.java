package org.spring.backendspring.admin.service.impl;

import java.util.List;

import org.spring.backendspring.admin.repository.AdminMemberRepository;
import org.spring.backendspring.admin.service.AdminMemberService;
import org.spring.backendspring.common.dto.PagedResponse;
import org.spring.backendspring.crew.crew.entity.CrewEntity;
import org.spring.backendspring.member.MemberMapper;
import org.spring.backendspring.member.dto.MemberDto;
import org.spring.backendspring.member.entity.MemberEntity;
import org.spring.backendspring.member.repository.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminMemberServiceImpl implements AdminMemberService {

    private final AdminMemberRepository adminMemberRepository;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    // 멤버 전체 조회
    @Override
    public PagedResponse<MemberDto> findAllMembers(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<MemberDto> memberPage;

        if (keyword == null || keyword.trim().isEmpty()) {
            // 검색어 없을 때 → 전체조회
            memberPage = memberRepository.findAll(pageable)
                    .map(MemberMapper::toDto);
        } else {
            // 검색어 있을 때 → 이메일 or 닉네임 검색
            memberPage = adminMemberRepository
                    .findByUserEmailContainingIgnoreCaseOrNickNameContainingIgnoreCase(keyword, keyword, pageable)
                    .map(MemberMapper::toDto);
        }

        return PagedResponse.of(memberPage);
    }

    @Override
    public void deleteMemberByAdmin(Long id) {
        MemberEntity member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("멤버를 찾을 수 없습니다."));
        memberRepository.delete(member);
    }
}
