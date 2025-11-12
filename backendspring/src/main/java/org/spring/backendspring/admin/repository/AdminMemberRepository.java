package org.spring.backendspring.admin.repository;

import org.spring.backendspring.member.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminMemberRepository extends JpaRepository<MemberEntity, Long> {

    
} 