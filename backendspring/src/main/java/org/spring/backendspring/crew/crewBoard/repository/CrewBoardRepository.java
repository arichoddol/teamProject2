package org.spring.backendspring.crew.crewBoard.repository;

import org.spring.backendspring.crew.crewBoard.entity.CrewBoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CrewBoardRepository extends JpaRepository<CrewBoardEntity, Long> {
    
}
