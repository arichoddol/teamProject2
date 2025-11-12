package org.spring.backendspring.member.repository;

import java.util.Optional;
import org.spring.backendspring.member.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    Optional<MemberEntity> findByUserEmail(String userEmail);

    Optional<MemberEntity> findById(Long memberId);
}
