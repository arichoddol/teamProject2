package org.spring.backendspring.item.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.spring.backendspring.board.dto.BoardDto;
import org.spring.backendspring.board.entity.BoardEntity;
import org.spring.backendspring.item.dto.ItemDto;
import org.spring.backendspring.item.entity.ItemEntity;
import org.spring.backendspring.item.repository.ItemImgRepository;
import org.spring.backendspring.item.repository.ItemRepository;
import org.spring.backendspring.item.service.ItemService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
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

        if (subject == null || search == null || search.equals("")) {
            itemEntities = itemRepository.findAll(pageable);
        } else {
            if (subject.equals("itemTitle")) {
                itemEntities = itemRepository.findByItemTitleContaining(pageable, search);
            } else if (subject.equals("itemDetail")) {
                itemEntities = itemRepository.findByItemDetailContaining(pageable, search);
            } else if (subject.equals("itemPrice")) {
                itemEntities = itemRepository.findByItemPriceContaining(pageable, search);
            } else {
                itemEntities = itemRepository.findAll(pageable);
            }
        }
        return itemEntities.map(ItemDto::toItemDto);
    }

    // category Paging
    @Override
    public Page<ItemDto> getItemsByCategory(Pageable pageable, String category, String subject, String search) {

        Page<ItemEntity> itemEntities = null;
        if (search != null && !search.isEmpty() && "itemTitle".equals(subject)) {
            itemEntities = itemRepository.findByCategoryAndItemTitleContaining(pageable, category, subject, search);
        } else {
            itemEntities = itemRepository.findByCategory(pageable, category);
        }
        return itemEntities.map(ItemDto::toItemDto);
    }

    @Override
    public ItemDto itemDetail(Long itemId) {
        //  BoardEntity boardEntity = boardRepository.findById(boardId)
        //         .orElseThrow(()-> new IllegalArgumentException("게시글 아이디가 존재하지 않음" + boardId));
        // return BoardDto.toBoardDto(boardEntity);        

        ItemEntity itemEntity = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("상품에 해당하는 아이디가 존재하지 않음"));
        return ItemDto.toItemDto(itemEntity);
    }


    // this method for Show recentlyItem.
    @Override
    public List<ItemDto> getRecentItem(){
    List<ItemEntity> recentlyentities = itemRepository.findTop2ByOrderByCreateTimeDesc();

    return recentlyentities.stream()
                        .map(ItemDto::toItemDto)
                        .collect(Collectors.toList());
    }
}
