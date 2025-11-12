package org.spring.backendspring.member.service;

import org.spring.backendspring.member.dto.MemberDto;

public interface MemberService  {

    void insertMember(MemberDto memberDto);

    MemberDto findByUserEmail(String userEmail);

    MemberDto findById(Long id);

    MemberDto updateMember(Long id, MemberDto updatedDto);

    void deleteMember(Long id);

   
}