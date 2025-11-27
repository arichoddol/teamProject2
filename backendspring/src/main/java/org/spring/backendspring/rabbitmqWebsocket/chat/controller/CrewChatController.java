package org.spring.backendspring.rabbitmqWebsocket.chat.controller;

import java.time.LocalDateTime;

import org.spring.backendspring.rabbitmqWebsocket.chat.dto.ChatMessageDto;
import org.spring.backendspring.rabbitmqWebsocket.chat.service.CrewChatService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CrewChatController {
    
    private final CrewChatService crewChatService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat/crew/{crewId}")
    public void sendCrewMessage(@DestinationVariable("crewId") Long crewId,
                                ChatMessageDto message) throws Exception {
        
                                    
        message.setCrewId(crewId);
        message.setCreateTime(LocalDateTime.now());
        crewChatService.saveMessage(message);
    
        messagingTemplate.convertAndSend("/topic/chat/crew/" + crewId, message);
        
    }

}
