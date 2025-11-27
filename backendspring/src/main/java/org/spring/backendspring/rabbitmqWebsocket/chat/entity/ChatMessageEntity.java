package org.spring.backendspring.rabbitmqWebsocket.chat.entity;

import java.time.LocalDateTime;

import org.spring.backendspring.common.BasicTime;
import org.spring.backendspring.rabbitmqWebsocket.chat.dto.ChatMessageDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "chat_message_tb")
public class ChatMessageEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sender_id", nullable = false)
    private Long senderId;
    
    @Column(name = "crew_id", nullable = false)
    private Long crewId;

    private String senderNickName;

    private String message;

    private LocalDateTime createTime;

    public static ChatMessageEntity toEntity(ChatMessageDto dto) {

        return ChatMessageEntity.builder()
                .senderId(dto.getSenderId())
                .crewId(dto.getCrewId())
                .senderNickName(dto.getSenderNickName())
                .message(dto.getMessage())
                .createTime(dto.getCreateTime())
                .build();
    }
}
