package org.spring.backendspring.crew.crewRun.repository;

import org.spring.backendspring.crew.crewRun.entity.CrewRunMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CrewRunMemberRepository extends JpaRepository<CrewRunMemberEntity,Long> {
    List<CrewRunMemberEntity> findAllByCrewRunEntityId(Long runId);

    void deleteByCrewRunEntityIdAndMemberEntityId(Long runId, Long memberId);

    Optional<CrewRunMemberEntity>findByCrewRunEntityIdAndMemberEntityId(Long runId, Long memberId);
}
