package org.spring.backendspring.rabbitmqWebsocket.chat.controller;

import org.spring.backendspring.rabbitmqWebsocket.chat.dto.ChatMessageDto;
import org.spring.backendspring.rabbitmqWebsocket.chat.rabbitmq.Sender;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ChatController {
    
    private final Sender sender;

    @MessageMapping("/chat.send")
    public void sendMessage(ChatMessageDto message) throws Exception {
        sender.send(message);
    }

}
