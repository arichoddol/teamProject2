package org.spring.backendspring.crew.crewJoin.repository;

import org.spring.backendspring.crew.crewJoin.entity.CrewJoinRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CrewJoinRequestRepository extends JpaRepository<CrewJoinRequestEntity, Long> {


    Optional<CrewJoinRequestEntity> findByCrewEntityIdAndMemberEntityId(Long crewId, Long memberId);

    List<CrewJoinRequestEntity> findAllByCrewEntityId(Long crewId);

    void deleteByCrewEntityIdAndMemberEntityId(Long crewId, Long memberId);

}
