package org.spring.backendspring.crew.crewRun.repository;

import org.spring.backendspring.crew.crewRun.entity.CrewRunMemberEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CrewRunMemberRepository extends JpaRepository<CrewRunMemberEntity,Long> {

    void deleteByCrewRunEntityIdAndMemberEntityId(Long runId, Long memberId);

    Optional<CrewRunMemberEntity>findByCrewRunEntityIdAndMemberEntityId(Long runId, Long memberId);

    Optional<CrewRunMemberEntity>findByMemberEntityId(Long memberId);

    Page<CrewRunMemberEntity> findAllByCrewRunEntityId(Long runId, Pageable pageable);
}
