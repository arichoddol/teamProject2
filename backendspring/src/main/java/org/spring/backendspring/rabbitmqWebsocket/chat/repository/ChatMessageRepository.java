package org.spring.backendspring.rabbitmqWebsocket.chat.repository;

import java.util.List;

import org.spring.backendspring.rabbitmqWebsocket.chat.entity.ChatMessageEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessageEntity, Long> {

    long countByCrewId(Long crewId);

    List<ChatMessageEntity> findByCrewId(Long crewId, Pageable pageable);

    List<ChatMessageEntity> findByCrewIdOrderByCreateTimeDesc(Long crewId, Pageable pageable);
    
}
