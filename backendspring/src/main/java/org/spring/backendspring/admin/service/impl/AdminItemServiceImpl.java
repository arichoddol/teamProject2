package org.spring.backendspring.admin.service.impl;

import org.spring.backendspring.admin.repository.AdminItemRepository;
import org.spring.backendspring.admin.service.AdminItemService;
import org.spring.backendspring.common.dto.PagedResponse;
import org.spring.backendspring.item.dto.ItemDto;
import org.spring.backendspring.item.entity.ItemEntity;
import org.spring.backendspring.item.repository.ItemRepository;
import org.spring.backendspring.member.dto.MemberDto;
import org.spring.backendspring.member.entity.MemberEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminItemServiceImpl implements AdminItemService {

    private final ItemRepository itemRepository;
    private final AdminItemRepository adminItemRepository;

    @Override
    public ItemDto findById(Long id) {
        return itemRepository.findById(id)
                .map(entity -> ItemDto.toItemDto(entity))
                .orElseThrow(() -> new EntityNotFoundException("해당 상품이 존재하지 않습니다"));
    }

    @Override
    public void insertItem(ItemDto itemDto) {
        ItemEntity item = ItemEntity.toItemEntity(itemDto);
        itemRepository.save(item);

    }

    @Override
    public ItemDto updateItem(Long id, ItemDto updatedDto) {
        ItemEntity existingItem = itemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 상품이 존재하지 않습니다"));
        // 업데이트 필드 설정

        String newFileName = (updatedDto.getNewFileName() != null && !updatedDto.getNewFileName().isEmpty())
                ? updatedDto.getNewFileName()
                : existingItem.getNewFileName();

        String oldFileName = (updatedDto.getOldFileName() != null && !updatedDto.getOldFileName().isEmpty())
                ? updatedDto.getOldFileName()
                : existingItem.getOldFileName();

        int attachFileValue = (newFileName != null && !newFileName.isEmpty()) ? 1 : 0;

        ItemEntity updatedEntity = ItemEntity.builder()
                .id(existingItem.getId())
                .itemTitle(updatedDto.getItemTitle())
                .itemDetail(updatedDto.getItemDetail())
                .itemPrice(updatedDto.getItemPrice())
                .itemSize(updatedDto.getItemSize())
                .attachFile(attachFileValue)
                .newFileName(newFileName)
                .oldFileName(oldFileName)
                .build();
        itemRepository.save(updatedEntity);
        return ItemDto.toItemDto(updatedEntity);
    }

    @Override
    public void deleteItem(Long id) {
        ItemEntity item = itemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 상품이 존재하지 않습니다"));
        itemRepository.delete(item);
    }

    @Override
    public PagedResponse<ItemDto> findAllItems(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<ItemDto> itemPage;

        if (keyword == null || keyword.trim().isEmpty()) {
            itemPage = itemRepository.findAll(pageable)
                    .map(ItemDto::toItemDto);
        } else {
            itemPage = adminItemRepository
                    .findByItemTitleContainingIgnoreCaseOrItemDetailContainingIgnoreCase(keyword, keyword, pageable)
                    .map(ItemDto::toItemDto);
        }

        return PagedResponse.of(itemPage);
    }
}
