package org.spring.backendspring.crew.crewMember.repository;

import org.spring.backendspring.crew.crewMember.entity.CrewMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CrewMemberRepository extends JpaRepository<CrewMemberEntity, Long> {

    Optional<CrewMemberEntity> findByCrewEntityIdAndMemberEntityId(Long crewId, Long memberId);

    List<CrewMemberEntity> findAllByCrewEntityId(Long crewId);

    List<CrewMemberEntity> findByMemberEntity_id(Long memberId);
}
