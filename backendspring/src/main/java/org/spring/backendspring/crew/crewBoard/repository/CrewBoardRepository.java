package org.spring.backendspring.crew.crewBoard.repository;

import java.util.List;
import java.util.Optional;

import org.spring.backendspring.crew.crewBoard.entity.CrewBoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CrewBoardRepository extends JpaRepository<CrewBoardEntity, Long> {

    List<CrewBoardEntity> findByCrewEntity_Id(Long crewId);

    Optional<CrewBoardEntity> findByCrewEntity_IdAndId(Long crewId, Long id);
    
}
