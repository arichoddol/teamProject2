package org.spring.backendspring.admin.service;

import java.util.List;

import org.spring.backendspring.member.dto.MemberDto;


public interface AdminMemberService {
    
     List<MemberDto> findAllMembers();
}
