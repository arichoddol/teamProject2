package org.spring.backendspring.crew.crewJoin.repository;

import org.spring.backendspring.crew.crewJoin.entity.CrewJoinRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CrewJoinRequestRepository extends JpaRepository<CrewJoinRequestEntity, Long> {


    Optional<CrewJoinRequestEntity> findByCrewEntityIdAndMemberEntityId(Long crewId, Long memberId);

    List<CrewJoinRequestEntity> findAllByCrewEntityId(Long crewId);
    // Page<CrewJoinRequestEntity> findAllByCrewEntityId(Long crewId, Pageable pageable);

    void deleteByCrewEntityIdAndMemberEntityId(Long crewId, Long memberId);

    boolean existsByMemberEntity_Id(Long id);

    void deleteByMemberEntity_Id(Long id);
}
