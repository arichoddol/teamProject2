package org.spring.backendspring.rabbitmqWebsocket.chat.rabbitmqService;

import org.spring.backendspring.rabbitmqWebsocket.chat.dto.BotMessageDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import kr.co.shineware.nlp.komoran.model.Token;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class MyCrewBotService {
    @Value("${spring.rabbitmq.crew.exchange}")
    private String crewExchangeYml;
    
    private final RabbitTemplate rabbitTemplate;
    private final Komoran komoran = new Komoran(DEFAULT_MODEL.FULL);
    public void sendCrewBot(BotMessageDto botMessageDto) {

        KomoranResult komoranResult = komoran.analyze(botMessageDto.getText());
        List<Token> tokens = komoranResult.getTokenList();
        String routingKey = "crew." + botMessageDto.getCrewId() + "." + botMessageDto.getMemberId();
        String text = "";

        boolean first = false;
        boolean today = false;
        boolean run = false;
        boolean schedule = false;
        for (Token token : tokens) {
            String botMsgNnp = token.getMorph();
            log.info("====={}=====", botMsgNnp);
            
            if (botMsgNnp.equals("hellow")) first = true;
            if (botMsgNnp.equals("오늘")) today = true;
            if (botMsgNnp.equals("런닝")) run = true;
            if (botMsgNnp.equals("일정") || botMsgNnp.equals("스케줄")) schedule = true;
        }
       log.info("========서비스까지옴======"); 
       if (first) {
           text =  "어서오세요!" + botMessageDto.getMemberNickName() 
           + "님 찾고싶은거 있으면 말해주세요 🚀";
           log.info("========hellow인식함======"); 
        } 
        
        else {
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
