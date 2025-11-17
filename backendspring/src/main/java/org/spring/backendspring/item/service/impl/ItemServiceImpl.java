package org.spring.backendspring.item.service.impl;

import org.spring.backendspring.board.dto.BoardDto;
import org.spring.backendspring.board.entity.BoardEntity;
import org.spring.backendspring.item.dto.ItemDto;
import org.spring.backendspring.item.entity.ItemEntity;
import org.spring.backendspring.item.repository.ItemImgRepository;
import org.spring.backendspring.item.repository.ItemRepository;
import org.spring.backendspring.item.service.ItemService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    // Bean Injection
    private final ItemRepository itemRepository;
    private final ItemImgRepository imgRepository;

    @Override
    public Page<ItemDto> pagingSearchItemList(Pageable pageable, String subject, String search) {
       
        // init 
        Page<ItemEntity> itemEntities = null;
        if(subject==null || search==null || search.equals("")){
            itemEntities = itemRepository.findAll(pageable);
        } else {
            if(subject.equals("itemTitle")){
                itemEntities = itemRepository.findByItemTitleContaining(pageable, search);
            } else if (subject.equals("itemDetail")) {
                itemEntities = itemRepository.findByItemDetailContaining(pageable, search);
            } else if (subject.equals("itemPrice")) {
                itemEntities = itemRepository.findByItemPriceContaining(pageable, search);
            } else {
                itemEntities = itemRepository.findAll(pageable);
            }
        }
        return itemEntities.map(el->{
            int itemSize = 0; 
    
            return ItemDto.builder()    
                        .id(el.getId())
                        .itemTitle(el.getItemTitle())
                        .itemDetail(el.getItemDetail())
                        .itemPrice(el.getItemPrice())
                        .itemSize(el.getItemSize())
                        .attachFile(el.getAttachFile())
                        .memberId(el.getMemberEntity().getId())
                        .createTime(el.getCreateTime())
                        .updateTime(el.getUpdateTime())
                     .build();
        });   
    }

    @Override
    public ItemDto itemDetail(Long itemId) {
        //  BoardEntity boardEntity = boardRepository.findById(boardId)
        //         .orElseThrow(()-> new IllegalArgumentException("게시글 아이디가 존재하지 않음" + boardId));
        // return BoardDto.toBoardDto(boardEntity);        

        ItemEntity itemEntity = itemRepository.findById(itemId)
                .orElseThrow(()-> new IllegalArgumentException("상품에 해당하는 아이디가 존재하지 않음"));
        return ItemDto.toItemDto(itemEntity);
    }

    // description ServiceImpl funtion 
    // escapcially AWS S3 bucket function must be Implement

    
}
