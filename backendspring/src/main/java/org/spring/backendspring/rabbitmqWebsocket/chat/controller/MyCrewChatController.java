package org.spring.backendspring.rabbitmqWebsocket.chat.controller;

import java.time.LocalDateTime;

import org.spring.backendspring.rabbitmqWebsocket.chat.dto.ChatMessageDto;
import org.spring.backendspring.rabbitmqWebsocket.chat.webSocketService.CrewChatService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MyCrewChatController {
    
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

    @MessageMapping("/chat/crew/{crewId}/enter")
    public void enter(@DestinationVariable("crewId") Long crewId,
                      ChatMessageDto message) throws Exception {
                                            
        ChatMessageDto saved = crewChatService.enterChat(crewId, message.getSenderId());
    
        messagingTemplate.convertAndSend("/topic/chat/crew/" + crewId, saved);
        
    }
    @MessageMapping("/chat/crew/{crewId}/leave")
    public void leave(@DestinationVariable("crewId") Long crewId,
                                ChatMessageDto message) throws Exception {
        
                                    
        ChatMessageDto saved = crewChatService.leaveChat(crewId, message.getSenderId());
    
        messagingTemplate.convertAndSend("/topic/chat/crew/" + crewId, saved);
        
    }

}
