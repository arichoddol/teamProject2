package org.spring.backendspring.member.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import org.spring.backendspring.crew.crewCreate.repository.CrewCreateRequestRepository;
import org.spring.backendspring.crew.crewJoin.repository.CrewJoinRequestRepository;
import org.spring.backendspring.member.MemberMapper;
import org.spring.backendspring.member.dto.MemberDto;
import org.spring.backendspring.member.entity.MemberEntity;
import org.spring.backendspring.member.entity.MemberProfileImageEntity;
import org.spring.backendspring.member.repository.MemberProfileImageRepository;
import org.spring.backendspring.member.repository.MemberRepository;
import org.spring.backendspring.member.service.MemberService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberProfileImageRepository memberProfileImageRepository;
    private final CrewJoinRequestRepository crewJoinRequestRepository;
    private final CrewCreateRequestRepository crewCreateRequestRepository;

    @Value("${file.upload-dir}")
    public String filePath;

    @Override
    public int userEmailCheck(String userEmail) {
        Optional<MemberEntity> byUserEmail = memberRepository.findByUserEmail(userEmail);
        return byUserEmail.isPresent() ? 0 : 1;
    }

    @Override
    public MemberDto findByUserEmail(String userEmail) {
        return memberRepository.findByUserEmail(userEmail)
                .map(MemberMapper::toDto)
                .orElse(null);
    }

    @Override
    public void insertMember(MemberDto memberDto, MultipartFile memberFile) {

        memberRepository.findByUserEmail(memberDto.getUserEmail())
                .ifPresent((email) -> {
                    throw new IllegalArgumentException("이미 존재하는 이메일입니다. " + email);
                });

        MemberEntity memberEntity = MemberMapper.toEntity(memberDto, passwordEncoder);
        memberEntity.setIsProfileImg(memberFile != null ? 1 : 0);
        memberRepository.save(memberEntity);

        if (memberFile != null) {
            String originalFileName = memberFile.getOriginalFilename();
            String newFileName = UUID.randomUUID() + "_" + originalFileName;

            try {
                memberFile.transferTo(new File(filePath, newFileName));
            } catch (IOException e) {
                throw new RuntimeException("프로필 이미지 저장 실패 ", e);
            }

            memberProfileImageRepository.save(
                    MemberProfileImageEntity.builder()
                            .oldName(originalFileName)
                            .newName(newFileName)
                            .memberEntity(memberEntity)
                            .build());
        }
    }

    // 개인(본인) 회원 조회
    @Override
    public MemberDto findById(Long id) {
        return memberRepository.findById(id)

                // NPE Error so ill fix this
                .map(MemberMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("해당 회원이 존재하지 않습니다"));
    }

    @Override
    public MemberDto updateMember(Long id, MemberDto updatedDto, MultipartFile memberFile) {
        MemberEntity memberEntity = memberRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 회원이 존재하지 않습니다"));

        if (updatedDto.getUserPassword() != null) {
            memberEntity.setUserPassword(passwordEncoder.encode(updatedDto.getUserPassword()));
        }

        if (memberFile != null) {
            String originalFileName = memberFile.getOriginalFilename();
            String newFileName = UUID.randomUUID() + "_" + originalFileName;

            try {
                memberFile.transferTo(new File(filePath, newFileName));
            } catch (IOException e) {
                throw new RuntimeException("프로필 이미지 저장 실패 ", e);
            }

            Optional<MemberProfileImageEntity> memberImg =
                    memberProfileImageRepository.findByMemberEntity_id(memberEntity.getId());

            if (memberImg.isEmpty()) {
                memberProfileImageRepository.save(MemberProfileImageEntity.builder()
                        .oldName(originalFileName)
                        .newName(newFileName)
                        .memberEntity(memberEntity)
                        .build());
                memberEntity.setIsProfileImg(1);
            } else {
                String oldFile = memberImg.get().getNewName();
                File deleteFile = new File(filePath, oldFile);
                if (deleteFile.exists()) {
                    deleteFile.delete();
                }
                memberImg.get().setOldName(originalFileName);
                memberImg.get().setNewName(newFileName);
                memberImg.get().setMemberEntity(memberEntity);
            }
        } else {
            memberProfileImageRepository.findByMemberEntity_id(memberEntity.getId())
                    .ifPresent(memberImg -> {
                        String oldFile = memberImg.getNewName();
                        File deleteFile = new File(filePath, oldFile);
                        if (deleteFile.exists()) {
                            deleteFile.delete();
                        }
                        memberProfileImageRepository.delete(memberImg);
                    });
        }
        MemberEntity updateEntity = MemberMapper.toUpdateEntity(updatedDto, memberEntity);
        return MemberMapper.toDto(memberRepository.save(updateEntity));
    }

    @Override
    public void deleteMember(Long id) {
        MemberEntity member = memberRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 회원이 존재하지 않습니다"));
        if (member.getIsProfileImg() == 1) {
            File oldFile = new File(filePath, member.getProfileImagesList().get(0).getNewName());
            if (oldFile.exists()) {
                oldFile.delete();
            }
            memberProfileImageRepository.deleteById(member.getProfileImagesList().get(0).getId());
        }
        // 크루 join 신청 데이터 삭제
        if (crewJoinRequestRepository.existsByMemberEntity_Id(member.getId())) {
            crewJoinRequestRepository.deleteByMemberEntity_Id(member.getId());
        }
        // 크루 생성 신청 데이터 삭제
        if (crewCreateRequestRepository.existsByMemberEntity_Id(member.getId())) {
            crewCreateRequestRepository.deleteByMemberEntity_Id(member.getId());
        }
        memberRepository.delete(member);
    }
}
