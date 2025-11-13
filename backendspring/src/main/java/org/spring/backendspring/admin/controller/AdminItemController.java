package org.spring.backendspring.admin.controller;

import java.util.List;

import org.spring.backendspring.admin.service.AdminItemService;
import org.spring.backendspring.common.dto.PagedResponse;
import org.spring.backendspring.item.dto.ItemDto;
import org.spring.backendspring.item.service.ItemService;
import org.spring.backendspring.member.dto.MemberDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/item")
@RequiredArgsConstructor
public class AdminItemController {

    private final AdminItemService adminItemService;
    private final ItemService itemService;

    // 수정전 데이터 가져오기 위해서 detail 필요
    @GetMapping("/detail/{id}")
    public ResponseEntity<ItemDto> getItemDetail(@PathVariable Long id) {
        return ResponseEntity.ok(adminItemService.findById(id));
    }

    // itemService의 findAllItems 사용 X -> 공통 BasicPagingDto 클래스 만들어서 사용하는 방향으로
    @GetMapping("/itemList")
    public ResponseEntity<PagedResponse<ItemDto>> getAllItems(
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {

        PagedResponse<ItemDto> itemList = adminItemService.findAllItems(keyword, page, size);
        return ResponseEntity.ok(itemList);
    }

    @PostMapping("/insert")
    public ResponseEntity<?> insertItem(@RequestBody ItemDto itemDto) {
        adminItemService.insertItem(itemDto);
        return ResponseEntity.ok("관리자 상품 등록");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ItemDto> updateItem(@PathVariable Long id, @RequestBody ItemDto updatedDto) {
        ItemDto updatedItem = adminItemService.updateItem(id, updatedDto);
        return ResponseEntity.ok(updatedItem);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteItem(@PathVariable Long id) {
        adminItemService.deleteItem(id);
        return ResponseEntity.ok("관리자 상품 삭제 완료");
    }

}
