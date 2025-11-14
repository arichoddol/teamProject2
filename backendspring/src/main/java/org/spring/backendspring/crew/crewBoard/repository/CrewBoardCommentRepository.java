package org.spring.backendspring.crew.crewBoard.repository;

import java.util.List;
import java.util.Optional;

import org.spring.backendspring.crew.crewBoard.entity.CrewBoardCommentEntity;
import org.spring.backendspring.crew.crewBoard.entity.CrewBoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CrewBoardCommentRepository extends JpaRepository<CrewBoardCommentEntity, Long> {

    List<CrewBoardCommentEntity> findAllByCrewBoardEntityOrderByIdDesc(CrewBoardEntity boardEntity);

    Optional<CrewBoardCommentEntity> findByCrew_IdAndBoard_IdAndId(Long crewId, Long boardId, Long id);
    
}
