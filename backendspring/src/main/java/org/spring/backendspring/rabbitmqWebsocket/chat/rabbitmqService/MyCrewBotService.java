package org.spring.backendspring.rabbitmqWebsocket.chat.rabbitmqService;

import org.spring.backendspring.rabbitmqWebsocket.chat.dto.BotMessageDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class MyCrewBotService {
    @Value("${spring.rabbitmq.crew.exchange}")
    private String crewExchangeYml;
    
    private final RabbitTemplate rabbitTemplate;

    public void sendCrewBot(BotMessageDto botMessageDto) {
       
        String routingKey = "crew." + botMessageDto.getCrewId() + "." + botMessageDto.getMemberId();
        String text = "";
       log.info("========서비스까지옴======"); 
       if ("hellow".equals(botMessageDto.getText())) {
           text =  "어서오세요!" + botMessageDto.getMemberNickName() 
           + "님 찾고싶은거 있으면 말해주세요 🚀";
           log.info("========hellow인식함======"); 
        } else {
            text = "등록 되어있지 않은 정보 입니다 :( ";
        }

        
        
        
        BotMessageDto botMessageDto2 = BotMessageDto.builder()
        .crewId(botMessageDto.getCrewId())
        .memberId(botMessageDto.getMemberId())
        .memberNickName(botMessageDto.getMemberNickName())
        .text(text)
        .build();

        rabbitTemplate.convertAndSend(crewExchangeYml, routingKey, botMessageDto2);
    }


}
