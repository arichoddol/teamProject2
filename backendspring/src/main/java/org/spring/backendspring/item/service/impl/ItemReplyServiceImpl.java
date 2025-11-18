package org.spring.backendspring.item.service.impl;

import java.util.Optional;

import org.spring.backendspring.item.dto.ItemReplyDto;
import org.spring.backendspring.item.entity.ItemEntity;
import org.spring.backendspring.item.entity.ItemReplyEntity;
import org.spring.backendspring.item.repository.ItemReplyRepository;
import org.spring.backendspring.item.repository.ItemRepository;
import org.spring.backendspring.item.service.ItemReplyService;
import org.spring.backendspring.member.entity.MemberEntity;
import org.spring.backendspring.member.repository.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemReplyServiceImpl implements ItemReplyService {

    private final ItemReplyRepository itemReplyRepository;
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;

    @Override
    public Long insertReply(ItemReplyDto itemReplyDto) {
        // ItemIdCheck 
        Optional<ItemEntity> optionalItemEntity 
                = itemRepository.findById(itemReplyDto.getItemId());
        if(optionalItemEntity.isPresent()){
            ItemEntity itemEntity = optionalItemEntity.get();
            itemReplyDto.setItemEntity(itemEntity);

        // memberCheck
        if(itemReplyDto.getMemberId() == null){
            throw new IllegalArgumentException("존재하지 않는회원");
        }
        Optional<MemberEntity> optionalMemberEntitiy 
            = memberRepository.findById(itemReplyDto.getMemberId());
        if(!optionalMemberEntitiy.isPresent()){
            throw new IllegalArgumentException("존재하지 않는회원입니다.");
        }
        itemReplyDto.setMemberEntity(optionalMemberEntitiy.get());

        ItemReplyEntity itemReplyEntity = 
                ItemReplyEntity.toReplyEntity(itemReplyDto);
        return itemReplyRepository.save(itemReplyEntity).getId();
    }
    return null;
}

    @Override
    public Page<ItemReplyDto> getReplyPage(Long itemId, Pageable pageable) {

        Page<ItemReplyEntity> replyentititesPage = 
            itemReplyRepository.findAllByItemEntity_id(itemId, pageable);

        Page<ItemReplyDto> replyPage = replyentititesPage
                .map(ItemReplyDto::toItemReplyDto);
            
        return replyPage;


    }
    
}
