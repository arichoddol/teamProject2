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
import org.spring.backendspring.member.entity.MemberEntity;
import org.spring.backendspring.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminItemServiceImpl implements AdminItemService {

    private final ItemRepository itemRepository;
    private final AdminItemRepository adminItemRepository;
    private final ItemImgRepository itemImgRepository;
    private final MemberRepository memberRepository;

    // private final String uploadPath = "E:\\uploadImg\\";
    // private static final String uploadPath = "C:/full/upload/";
    private static final String uploadPath = "E:/full/upload/";

    // ===========================================================
    // FIND ONE
    // ===========================================================
    @Override
    public ItemDto findById(Long id) {
        return itemRepository.findById(id)
                .map(ItemDto::toItemDto)
                .orElseThrow(() -> new EntityNotFoundException("해당 상품이 존재하지 않습니다"));
    }

    // ===========================================================
    // INSERT
    // ===========================================================
    @Override
    public void insertItem(ItemDto itemDto, MultipartFile itemFile, Long memberId) {

        MemberEntity member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("멤버 없음"));

        // 1) 빌더로 엔티티 생성
        ItemEntity item = ItemEntity.builder()
                .itemTitle(itemDto.getItemTitle())
                .itemDetail(itemDto.getItemDetail())
                .itemPrice(itemDto.getItemPrice())
                .itemSize(itemDto.getItemSize())
                .category(itemDto.getCategory())
                .attachFile(0)
                .memberEntity(member)
                .build();

        String originalName = null;
        String newName = null;

        // 2) 파일 처리
        if (itemFile != null && !itemFile.isEmpty()) {

            File folder = new File(uploadPath);
            if (!folder.exists())
                folder.mkdirs();

            originalName = itemFile.getOriginalFilename();
            newName = UUID.randomUUID() + "_" + originalName;

            try {
                itemFile.transferTo(new File(uploadPath + newName));
            } catch (IOException e) {
                throw new RuntimeException("파일 저장 실패", e);
            }

            item.setOldFileName(originalName);
            item.setNewFileName(newName);
            item.setAttachFile(1);
        }

        // 3) 최종 save 딱 한 번
        itemRepository.save(item);

        // 4) 이미지 테이블 저장
        if (newName != null) {
            itemImgRepository.save(
                    ItemImgEntity.builder()
                            .itemEntity(item)
                            .oldName(originalName)
                            .newName(newName)
                            .build());
        }
    }

    // ===========================================================
    // UPDATE
    // ===========================================================
    @Override
    public ItemDto updateItem(Long id, ItemDto dto, MultipartFile itemFile, Long memberId) {

        ItemEntity old = itemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 상품이 존재하지 않습니다"));

        MemberEntity member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("멤버 없음"));

        ItemImgEntity oldImg = itemImgRepository.findByItemEntity(old);

        String originalName = null;
        String newName = null;

        /*
         * ===========================================
         * CASE 1 : 이미지 삭제 요청 (attachFile = 0)
         * ===========================================
         */
        if (dto.getAttachFile() == 0) {

            // 기존 이미지 삭제
            if (oldImg != null) {
                new File(uploadPath + oldImg.getNewName()).delete();
                itemImgRepository.delete(oldImg);
            }

            ItemEntity updated = ItemEntity.builder()
                    .id(old.getId())
                    .itemTitle(dto.getItemTitle())
                    .itemDetail(dto.getItemDetail())
                    .itemPrice(dto.getItemPrice())
                    .itemSize(dto.getItemSize())
                    .attachFile(0)
                    .category(dto.getCategory())
                    .oldFileName(null)
                    .newFileName(null)
                    .memberEntity(member)
                    .build();

            return ItemDto.toItemDto(itemRepository.save(updated));
        }

        /*
         * ===========================================
         * CASE 2 : 새 이미지 업로드
         * ===========================================
         */
        if (itemFile != null && !itemFile.isEmpty()) {

            // 기존 이미지 삭제
            if (oldImg != null) {
                new File(uploadPath + oldImg.getNewName()).delete();
                itemImgRepository.delete(oldImg);
            }

            File folder = new File(uploadPath);
            if (!folder.exists())
                folder.mkdirs();

            originalName = itemFile.getOriginalFilename();
            newName = UUID.randomUUID() + "_" + originalName;

            try {
                itemFile.transferTo(new File(uploadPath + newName));
            } catch (IOException e) {
                throw new RuntimeException("파일 저장 실패", e);
            }

            // 새 이미지 저장
            itemImgRepository.save(
                    ItemImgEntity.builder()
                            .itemEntity(old)
                            .oldName(originalName)
                            .newName(newName)
                            .build());
        }

        /*
         * ===========================================
         * CASE 3 : 파일 유지 / 또는 CASE2 끝난 후 최종 조립
         * ===========================================
         */
        ItemEntity updated = ItemEntity.builder()
                .id(old.getId())
                .itemTitle(dto.getItemTitle())
                .itemDetail(dto.getItemDetail())
                .itemPrice(dto.getItemPrice())
                .itemSize(dto.getItemSize())
                .attachFile(newName != null ? 1 : old.getAttachFile())
                .category(dto.getCategory())
                .oldFileName(newName != null ? originalName : old.getOldFileName())
                .newFileName(newName != null ? newName : old.getNewFileName())
                .memberEntity(member)
                .build();

        ItemEntity saved = itemRepository.save(updated);

        // 🔥 연관 이미지가 포함된 엔티티로 다시 조회해야 DTO에 itemImgDtos가 들어감
        ItemEntity loaded = itemRepository.findById(saved.getId())
                .orElseThrow(() -> new RuntimeException("업데이트 후 재조회 실패"));

        return ItemDto.toItemDto(loaded);

    }

    // ===========================================================
    // DELETE
    // ===========================================================
    @Override
    public void deleteItem(Long id) {

        ItemEntity item = itemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 상품이 존재하지 않습니다"));

        ItemImgEntity img = itemImgRepository.findByItemEntity(item);

        if (img != null) {
            new File(uploadPath + img.getNewName()).delete();
            itemImgRepository.delete(img);
        }

        itemRepository.delete(item);
    }

    @Override
    public void deleteImage(Long id) {
        ItemEntity item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("상품 없음"));

        item.setItemImage(null);
        item.setAttachFile(0);

        itemRepository.save(item);

    }

    // ===========================================================
    // FIND ALL
    // ===========================================================
    @Override
    public PagedResponse<ItemDto> findAllItems(String keyword, int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<ItemDto> itemPage;

        if (keyword == null || keyword.trim().isEmpty()) {
            itemPage = itemRepository.findAll(pageable).map(ItemDto::toItemDto);
        } else {
            itemPage = adminItemRepository
                    .findByItemTitleContainingIgnoreCaseOrItemDetailContainingIgnoreCase(keyword, keyword, pageable)
                    .map(ItemDto::toItemDto);
        }

        return PagedResponse.of(itemPage);
    }
}

// package org.spring.backendspring.admin.service.impl;

// import java.io.File;
// import java.io.IOException;
// import java.util.UUID;

// import org.spring.backendspring.admin.repository.AdminItemRepository;
// import org.spring.backendspring.admin.service.AdminItemService;
// import org.spring.backendspring.common.dto.PagedResponse;
// import org.spring.backendspring.item.dto.ItemDto;
// import org.spring.backendspring.item.entity.ItemEntity;
// import org.spring.backendspring.item.entity.ItemImgEntity;
// import org.spring.backendspring.item.repository.ItemImgRepository;
// import org.spring.backendspring.item.repository.ItemRepository;
// import org.spring.backendspring.member.dto.MemberDto;
// import org.spring.backendspring.member.entity.MemberEntity;
// import org.spring.backendspring.member.repository.MemberRepository;
// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.PageRequest;
// import org.springframework.data.domain.Pageable;
// import org.springframework.data.domain.Sort;
// import org.springframework.stereotype.Service;
// import org.springframework.web.multipart.MultipartFile;

// import jakarta.persistence.EntityNotFoundException;
// import lombok.RequiredArgsConstructor;

// @Service
// @RequiredArgsConstructor
// public class AdminItemServiceImpl implements AdminItemService {

// private final ItemRepository itemRepository;
// private final AdminItemRepository adminItemRepository;
// private final ItemImgRepository itemImgRepository;
// private final MemberRepository memberRepository;

// @Override
// public ItemDto findById(Long id) {
// return itemRepository.findById(id)
// .map(entity -> ItemDto.toItemDto(entity))
// .orElseThrow(() -> new EntityNotFoundException("해당 상품이 존재하지 않습니다"));
// }

// @Override
// public void insertItem(ItemDto itemDto, MultipartFile itemFile, Long
// memberId) {

// MemberEntity member = memberRepository.findById(memberId)
// .orElseThrow(() -> new RuntimeException("멤버 없음"));

// ItemEntity item = ItemEntity.toItemEntity(itemDto);

// item.setAttachFile(0); // 기본값 파일 이미지 없음

// item.setMemberEntity(member);

// if (itemFile == null || itemFile.isEmpty()) {
// return;
// }

// String uploadPath = "E:\\uploadImg\\";
// File folder = new File(uploadPath);

// if (!folder.exists()) {
// folder.mkdirs(); // 폴더가 없으면 자동 생성
// }

// String originalName = itemFile.getOriginalFilename();
// String newName = UUID.randomUUID() + "_" + originalName;

// try {
// itemFile.transferTo(new File(uploadPath + newName));
// } catch (IOException e) {
// throw new RuntimeException("파일 저장 실패", e);
// }

// // 6) 이미지 엔티티 저장
// ItemImgEntity img = ItemImgEntity.builder()
// .itemEntity(item)
// .oldName(originalName)
// .newName(newName)
// .build();

// itemRepository.save(item);

// item.setAttachFile(1);

// itemImgRepository.save(img);

// // 7) 첨부파일 상태 업데이트

// }

// @Override
// public ItemDto updateItem(Long id, ItemDto updatedDto, MultipartFile
// itemFile, Long memberId) {
// ItemEntity existingItem = itemRepository.findById(id)
// .orElseThrow(() -> new EntityNotFoundException("해당 상품이 존재하지 않습니다"));
// // 업데이트 필드 설정
// MemberEntity member = memberRepository.findById(memberId)
// .orElseThrow(() -> new RuntimeException("멤버 없음"));

// existingItem.setMemberEntity(member);

// ItemImgEntity oldImg = itemImgRepository.findByItemEntity(existingItem);
// String uploadPath = "E:\\uploadImg\\";

// if (updatedDto.getAttachFile() == 0) {
// // 기존 이미지 파일 삭제
// if (oldImg != null) {
// new File(uploadPath + oldImg.getNewName()).delete();
// itemImgRepository.delete(oldImg);
// }

// ItemEntity updatedEntity = ItemEntity.builder()
// .id(existingItem.getId())
// .itemTitle(updatedDto.getItemTitle())
// .itemDetail(updatedDto.getItemDetail())
// .itemPrice(updatedDto.getItemPrice())
// .itemSize(updatedDto.getItemSize())
// .attachFile(updatedDto.getAttachFile())
// .build();

// return ItemDto.toItemDto(itemRepository.save(updatedEntity));
// }
// // 이미지 파일이 있는 경우
// if (itemFile != null && !itemFile.isEmpty()) {
// // 기존 이미지 파일 삭제
// if (oldImg != null) {
// new File(uploadPath + oldImg.getNewName()).delete();
// itemImgRepository.delete(oldImg);
// }
// File folder = new File(uploadPath);
// if (!folder.exists())
// folder.mkdirs();

// String originalName = itemFile.getOriginalFilename();
// String newName = UUID.randomUUID() + "_" + originalName;

// try {
// itemFile.transferTo(new File(uploadPath + newName));
// } catch (Exception e) {
// throw new RuntimeException("파일 저장 실패", e);
// }

// // 새 이미지 저장
// itemImgRepository.save(
// ItemImgEntity.builder()
// .itemEntity(existingItem)
// .oldName(originalName)
// .newName(newName)
// .build());

// ItemEntity updatedEntity = ItemEntity.builder()
// .id(existingItem.getId())
// .itemTitle(updatedDto.getItemTitle())
// .itemDetail(updatedDto.getItemDetail())
// .itemPrice(updatedDto.getItemPrice())
// .itemSize(updatedDto.getItemSize())
// .attachFile(1)
// .build();

// return ItemDto.toItemDto(itemRepository.save(updatedEntity));
// }

// ItemEntity updatedEntity = ItemEntity.builder()
// .id(existingItem.getId())
// .itemTitle(updatedDto.getItemTitle())
// .itemDetail(updatedDto.getItemDetail())
// .itemPrice(updatedDto.getItemPrice())
// .itemSize(updatedDto.getItemSize())
// .attachFile(existingItem.getAttachFile()) // 그대로 유지
// .build();

// return ItemDto.toItemDto(itemRepository.save(updatedEntity));

// }

// @Override
// public void deleteItem(Long id) {
// ItemEntity item = itemRepository.findById(id)
// .orElseThrow(() -> new EntityNotFoundException("해당 상품이 존재하지 않습니다"));
// itemRepository.delete(item);
// }

// @Override
// public PagedResponse<ItemDto> findAllItems(String keyword, int page, int
// size) {
// Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
// Page<ItemDto> itemPage;

// if (keyword == null || keyword.trim().isEmpty()) {
// itemPage = itemRepository.findAll(pageable)
// .map(ItemDto::toItemDto);
// } else {
// itemPage = adminItemRepository
// .findByItemTitleContainingIgnoreCaseOrItemDetailContainingIgnoreCase(keyword,
// keyword, pageable)
// .map(ItemDto::toItemDto);
// }

// return PagedResponse.of(itemPage);
// }
// }
