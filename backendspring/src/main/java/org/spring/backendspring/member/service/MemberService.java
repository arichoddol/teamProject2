package org.spring.backendspring.member.service;

import org.spring.backendspring.member.dto.MemberDto;
import org.springframework.web.multipart.MultipartFile;

public interface MemberService  {

    void insertMember(MemberDto memberDto, MultipartFile memberFile);

    MemberDto findByUserEmail(String userEmail);

    MemberDto findById(Long id);

    MemberDto updateMember(Long id, MemberDto updatedDto, MultipartFile memberFile);

    void deleteMember(Long id);

   
}