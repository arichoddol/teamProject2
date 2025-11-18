package org.spring.backendspring.admin.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.spring.backendspring.admin.repository.AdminItemRepository;
import org.spring.backendspring.admin.service.AdminItemService;
import org.spring.backendspring.common.dto.PagedResponse;
import org.spring.backendspring.item.dto.ItemDto;
import org.spring.backendspring.item.entity.ItemEntity;
import org.spring.backendspring.item.entity.ItemImgEntity;
import org.spring.backendspring.item.repository.ItemImgRepository;
import org.spring.backendspring.item.repository.ItemRepository;
import org.spring.backendspring.member.dto.MemberDto;
import org.spring.backendspring.member.entity.MemberEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminItemServiceImpl implements AdminItemService {

    private final ItemRepository itemRepository;
    private final AdminItemRepository adminItemRepository;
    private final ItemImgRepository itemImgRepository;

    @Override
    public ItemDto findById(Long id) {
        return itemRepository.findById(id)
                .map(entity -> ItemDto.toItemDto(entity))
                .orElseThrow(() -> new EntityNotFoundException("해당 상품이 존재하지 않습니다"));
    }

    @Override
    public void insertItem(ItemDto itemDto, MultipartFile itemFile) {
        ItemEntity item = ItemEntity.toItemEntity(itemDto);
        item.setAttachFile(0); // 기본값 파일 이미지 없음
        itemRepository.save(item);

        if (itemFile == null || itemFile.isEmpty()) {
            return;
        }

        String uploadPath = "E:\\uploadImg\\";
        File folder = new File(uploadPath);

        if (!folder.exists()) {
            folder.mkdirs(); // 폴더가 없으면 자동 생성
        }

        String originalName = itemFile.getOriginalFilename();
        String newName = UUID.randomUUID() + "_" + originalName;

        try {
            itemFile.transferTo(new File(uploadPath + newName));
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패", e);
        }

        // 6) 이미지 엔티티 저장
        ItemImgEntity img = ItemImgEntity.builder()
                .itemEntity(item)
                .oldName(originalName)
                .newName(newName)
                .build();

        itemImgRepository.save(img);

        // 7) 첨부파일 상태 업데이트
        item.setAttachFile(1);
        itemRepository.save(item);

    }

    @Override
    public ItemDto updateItem(Long id, ItemDto updatedDto, MultipartFile itemFile) {
        ItemEntity existingItem = itemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 상품이 존재하지 않습니다"));
        // 업데이트 필드 설정

        ItemImgEntity oldImg = itemImgRepository.findByItemEntity(existingItem);
        String uploadPath = "E:\\uploadImg\\";

        if (updatedDto.getAttachFile() == 0) {
            // 기존 이미지 파일 삭제
            if (oldImg != null) {
                new File(uploadPath + oldImg.getNewName()).delete();
                itemImgRepository.delete(oldImg);
            }

            ItemEntity updatedEntity = ItemEntity.builder()
                    .id(existingItem.getId())
                    .itemTitle(updatedDto.getItemTitle())
                    .itemDetail(updatedDto.getItemDetail())
                    .itemPrice(updatedDto.getItemPrice())
                    .itemSize(updatedDto.getItemSize())
                    .attachFile(updatedDto.getAttachFile())
                    .build();

            return ItemDto.toItemDto(itemRepository.save(updatedEntity));
        }
        // 이미지 파일이 있는 경우
        if (itemFile != null && !itemFile.isEmpty()) {
            // 기존 이미지 파일 삭제
            if (oldImg != null) {
                new File(uploadPath + oldImg.getNewName()).delete();
                itemImgRepository.delete(oldImg);
            }
            File folder = new File(uploadPath);
            if (!folder.exists())
                folder.mkdirs();

            String originalName = itemFile.getOriginalFilename();
            String newName = UUID.randomUUID() + "_" + originalName;

            try {
                itemFile.transferTo(new File(uploadPath + newName));
            } catch (Exception e) {
                throw new RuntimeException("파일 저장 실패", e);
            }

            // 새 이미지 저장
            itemImgRepository.save(
                    ItemImgEntity.builder()
                            .itemEntity(existingItem)
                            .oldName(originalName)
                            .newName(newName)
                            .build());

            ItemEntity updatedEntity = ItemEntity.builder()
                    .id(existingItem.getId())
                    .itemTitle(updatedDto.getItemTitle())
                    .itemDetail(updatedDto.getItemDetail())
                    .itemPrice(updatedDto.getItemPrice())
                    .itemSize(updatedDto.getItemSize())
                    .attachFile(1)
                    .build();

            return ItemDto.toItemDto(itemRepository.save(updatedEntity));
        }

        ItemEntity updatedEntity = ItemEntity.builder()
                .id(existingItem.getId())
                .itemTitle(updatedDto.getItemTitle())
                .itemDetail(updatedDto.getItemDetail())
                .itemPrice(updatedDto.getItemPrice())
                .itemSize(updatedDto.getItemSize())
                .attachFile(existingItem.getAttachFile()) // 그대로 유지
                .build();

        return ItemDto.toItemDto(itemRepository.save(updatedEntity));

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
