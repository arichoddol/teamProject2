package org.spring.backendspring.rabbitmqWebsocket.chat.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessageDto {
    
    private Long crewId;
    private Long senderId;
    private String senderName;
    private String content;
    private LocalDateTime sentAt;
}
