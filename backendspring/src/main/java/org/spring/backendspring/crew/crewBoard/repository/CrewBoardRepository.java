package org.spring.backendspring.crew.crewBoard.repository;

import java.util.Optional;

import org.spring.backendspring.crew.crewBoard.entity.CrewBoardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CrewBoardRepository extends JpaRepository<CrewBoardEntity, Long> {

    Page<CrewBoardEntity> findByCrewEntity_Id(Long crewId, Pageable pageable);

    Optional<CrewBoardEntity> findByCrewEntity_IdAndId(Long crewId, Long id);

    Page<CrewBoardEntity> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase
            (String titleKeyword, String contentKeyword, Pageable pageable);
    
}
