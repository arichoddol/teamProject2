package org.spring.backendspring.member.service.impl;

import org.spring.backendspring.member.MemberMapper;
import org.spring.backendspring.member.dto.MemberDto;
import org.spring.backendspring.member.entity.MemberEntity;
import org.spring.backendspring.member.repository.MemberRepository;
import org.spring.backendspring.member.service.MemberService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public MemberDto findByUserEmail(String userEmail) {
        return memberRepository.findByUserEmail(userEmail)
                .map(entity -> MemberMapper.toDto(entity))
                .orElse(null);
    }

    @Override
    public void insertMember(MemberDto memberDto) {
        MemberEntity member = MemberMapper.toEntity(memberDto, passwordEncoder);

        memberRepository.save(member);
    }

    // 개인(본인) 회원 조회
    @Override
    public MemberDto findById(Long id) {
        // TODO: Security 인증 연동 시, 로그인 유저 id 비교 필요
        return memberRepository.findById(id)
                .map(entity -> MemberMapper.toDto(entity))
                .orElseThrow(() -> new EntityNotFoundException("해당 회원이 존재하지 않습니다"));
    }


    // 회원 수정 서비스
    @Override
    public MemberDto updateMember(Long id, MemberDto updatedDto) {
        MemberEntity existingMember = memberRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 회원이 존재하지 않습니다"));

        // 업데이트할 필드 설정
        // TODO: 비밀번호 변경은 별도 엔드포인트로 분리
        MemberEntity updatedEntity = MemberEntity.builder()
                .id(existingMember.getId())
                .userEmail(existingMember.getUserEmail())
                // .userPassword(updatedDto.getUserPassword() != null && !updatedDto.getUserPassword().isBlank() ?
                //         passwordEncoder.encode(updatedDto.getUserPassword()) :
                //         existingMember.getUserPassword())
                .userPassword(existingMember.getUserPassword())
                .userName(updatedDto.getUserName())
                .nickName(updatedDto.getNickName())
                .age(existingMember.getAge())
                .gender(existingMember.getGender())
                .address(updatedDto.getAddress())
                .phone(updatedDto.getPhone())
                .socialLogin(updatedDto.getSocialLogin())
                .role(existingMember.getRole())
                .isProfileImg(updatedDto.getIsProfileImg())
                .build();

        MemberEntity saved = memberRepository.save(updatedEntity);
        return MemberMapper.toDto(saved);
    }

    @Override
    public void deleteMember(Long id) {
        MemberEntity member = memberRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 회원이 존재하지 않습니다"));
        memberRepository.delete(member);
    }
}
