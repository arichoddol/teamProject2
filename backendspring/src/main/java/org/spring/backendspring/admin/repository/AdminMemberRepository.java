package org.spring.backendspring.admin.repository;

import org.spring.backendspring.member.entity.MemberEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.util.Streamable;

public interface AdminMemberRepository extends JpaRepository<MemberEntity, Long> {

    // search 용도
    Page<MemberEntity> findByUserEmailContainingIgnoreCaseOrNickNameContainingIgnoreCase(
            String emailKeyword,
            String nickKeyword,
            Pageable pageable);

}