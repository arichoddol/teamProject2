package org.spring.backendspring.rabbitmqWebsocket.chat.service;

import java.util.List;

import org.spring.backendspring.rabbitmqWebsocket.chat.dto.ChatMessageDto;

public interface CrewChatService {

    ChatMessageDto saveMessage(ChatMessageDto message);

    List<ChatMessageDto> recentMessages(Long crewId, int limit);
    
}
