package org.spring.backendspring.rabbitmqWebsocket.chat.controller;

import java.security.Principal;

import org.spring.backendspring.member.repository.MemberRepository;
import org.spring.backendspring.rabbitmqWebsocket.chat.dto.ChatMessageDto;
import org.spring.backendspring.rabbitmqWebsocket.chat.rabbitmq.Sender;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ChatController {
    
    private final Sender sender;
    private final MemberRepository memberRepository;

    @MessageMapping("/crew/{crewId}/send")
    public void sendMessage(@DestinationVariable Long crewId,
                            ChatMessageDto message,
                            @Header("simpUser") Principal principal
                            ) throws Exception {
        String username = principal.getName();

        if (!memberRepository.findByUserNameAndCrewEntityList_Id(username, crewId)) {
            throw new IllegalArgumentException("가입하지 않은 멤버");
        }

        
        sender.send(message);
    }

}
