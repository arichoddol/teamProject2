package org.spring.backendspring.rabbitmqWebsocket.chat.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberNotificationDto {

    private Long memberId;
    private String type;
    private String title;
    private String content;
    private LocalDateTime time;
}