package org.spring.backendspring.rabbitmqWebsocket.chat.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.spring.backendspring.config.security.MyUserDetails;
import org.spring.backendspring.rabbitmqWebsocket.chat.dto.ChatMessageDto;
import org.spring.backendspring.rabbitmqWebsocket.chat.service.CrewChatService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/mycrew/{crewId}/chat")
@RequiredArgsConstructor
public class CrewChatRestController {
    
    private final CrewChatService crewChatService;

    @GetMapping("/recent")
    public List<ChatMessageDto> recentMessages(@PathVariable("crewId") Long crewId,
                                               @RequestParam(name = "limit", defaultValue = "300") int limit) {
        return crewChatService.recentMessages(crewId, limit);
    }

    @PostMapping("/send")
    public ChatMessageDto sendmessage(@RequestBody ChatMessageDto dto,
                                      @PathVariable Long crewId,
                                      @AuthenticationPrincipal MyUserDetails userDetails) {
        LocalDateTime now = LocalDateTime.now();
        dto.setCrewId(crewId);
        dto.setSenderId(userDetails.getMemberId());
        dto.setSenderNickName(userDetails.getNickName());
        dto.setCreateTime(now);
        
        return crewChatService.saveMessage(dto);
    }
    
                                            
    
}
