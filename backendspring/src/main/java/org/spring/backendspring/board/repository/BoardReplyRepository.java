package org.spring.backendspring.board.repository;

import org.spring.backendspring.board.entity.BoardReplyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardReplyRepository extends JpaRepository<BoardReplyEntity, Long>
{
    
}
