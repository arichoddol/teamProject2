package org.spring.backendspring.rabbitmqWebsocket.chat.rabbitmq;

import org.spring.backendspring.member.entity.MemberEntity;
import org.spring.backendspring.member.repository.MemberRepository;
import org.spring.backendspring.rabbitmqWebsocket.chat.dto.ChatMessageDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class Sender {
    
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    public void send(ChatMessageDto message) {

        Long crewId = message.getCrewId();

        message.setCrewId(crewId);

        String routingKey = "chat.key.crew" + crewId;

        rabbitTemplate.convertAndSend(exchangeName, routingKey, message);
        System.out.println("보냄 :" + message);
    }
}
