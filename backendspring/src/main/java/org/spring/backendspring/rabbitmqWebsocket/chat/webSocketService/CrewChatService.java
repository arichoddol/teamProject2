package org.spring.backendspring.rabbitmqWebsocket.chat.webSocketService;

import java.io.IOException;
import java.util.List;

import org.spring.backendspring.rabbitmqWebsocket.chat.dto.ChatMessageDto;

public interface CrewChatService {

    ChatMessageDto saveMessage(ChatMessageDto message) throws IOException;

    List<ChatMessageDto> recentMessages(Long crewId, int limit);

    ChatMessageDto enterChat(Long crewId, Long memberId) throws IOException;

    ChatMessageDto leaveChat(Long crewId, Long memberId) throws IOException;
    
}
