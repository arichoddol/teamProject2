package org.spring.backendspring.rabbitmqWebsocket.chat.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.spring.backendspring.crew.crewMember.entity.CrewMemberEntity;
import org.spring.backendspring.crew.crewMember.repository.CrewMemberRepository;
import org.spring.backendspring.rabbitmqWebsocket.chat.dto.ChatMessageDto;
import org.spring.backendspring.rabbitmqWebsocket.chat.entity.ChatMessageEntity;
import org.spring.backendspring.rabbitmqWebsocket.chat.repository.ChatMessageRepository;
import org.spring.backendspring.rabbitmqWebsocket.chat.service.CrewChatService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CrewChatServiceImpl implements CrewChatService {
    
    private final ChatMessageRepository chatMessageRepository;
    private final CrewMemberRepository crewMemberRepository;

    @Override
    public ChatMessageDto saveMessage(ChatMessageDto message) {
        CrewMemberEntity crewMember = 
            crewMemberRepository.findByCrewEntityIdAndMemberEntityId(message.getCrewId(), message.getSenderId())
                .orElseThrow(() -> new IllegalArgumentException("해당 크루의 멤버가 아닙니다."));

        ChatMessageEntity entity = ChatMessageEntity.toEntity(message);

        ChatMessageEntity saved = chatMessageRepository.save(entity);

        long count = chatMessageRepository.countByCrewId(message.getCrewId());
        if (count > 300) {
            long toDelete = count - 300;
            List<ChatMessageEntity> oldest = chatMessageRepository.findByCrewId(
                message.getCrewId(),
                PageRequest.of(0, (int) toDelete, Sort.by("createTime").ascending())
            );
            chatMessageRepository.deleteAll(oldest);
        }
        
        return ChatMessageDto.toDto(saved);
    }

    @Override
    public List<ChatMessageDto> recentMessages(Long crewId, int limit) {
        List<ChatMessageEntity> list = chatMessageRepository.findByCrewId(
            crewId,
            PageRequest.of(0, limit, Sort.by("createTime").descending())
        );

        return list.stream().map(ChatMessageDto::toDto).collect(Collectors.toList());
    }
    
}
