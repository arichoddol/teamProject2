package org.spring.backendspring.rabbitmqWebsocket.chat.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.spring.backendspring.rabbitmqWebsocket.chat.entity.ChatMessageEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ChatMessageDto {
    
    private Long id;
    private Long crewId;
    private Long senderId;
    private String senderNickName;
    private String message;
    private String type;
    private LocalDateTime createTime;

    public static ChatMessageDto toDto(ChatMessageEntity entity) {
        
        return ChatMessageDto.builder()
                .id(entity.getId())
                .crewId(entity.getCrewId())
                .senderId(entity.getSenderId())
                .senderNickName(entity.getSenderNickName())
                .message(entity.getMessage())
                .createTime(entity.getCreateTime())
                .type(entity.getType())
                .build();
    }
}
