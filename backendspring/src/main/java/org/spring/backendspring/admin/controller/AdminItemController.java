package org.spring.backendspring.admin.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.spring.backendspring.admin.service.AdminItemService;
import org.spring.backendspring.board.dto.BoardDto;
import org.spring.backendspring.common.dto.PagedResponse;
import org.spring.backendspring.config.security.MyUserDetails;
import org.spring.backendspring.item.dto.ItemDto;
import org.spring.backendspring.item.service.ItemService;
import org.spring.backendspring.member.dto.MemberDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/item")
@RequiredArgsConstructor
public class AdminItemController {

    private final AdminItemService adminItemService;
    private final ItemService itemService;

    // 수정전 데이터 가져오기 위해서 detail 필요
    @GetMapping("/detail/{id}")
    public ResponseEntity<ItemDto> getItemDetail(@PathVariable("id") Long id) {
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
    public ResponseEntity<?> insertItem(
            @RequestPart("itemDto") ItemDto itemDto,
            @RequestPart("itemFile") MultipartFile itemFile,
            @AuthenticationPrincipal MyUserDetails myUserDetails,
            BindingResult bindingResult ) {

    if (bindingResult.hasErrors()) {
        Map<String, Object> errors = new HashMap<>();
        bindingResult.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), err.getDefaultMessage());
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
    try {
        Long memberId = myUserDetails.getMemberId();
        adminItemService.insertItem(itemDto, itemFile, memberId);
    
        return ResponseEntity.status(HttpStatus.CREATED).body("아이템 등록 성공");

    } catch (RuntimeException e) {
        // -> from ServiceLayer 
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("아이템 등록 실패: " + e.getMessage());
    }

    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateItem(@ModelAttribute ItemDto itemDto, 
                                        @AuthenticationPrincipal MyUserDetails myUserDetails) throws IOException {
        Long memberId = myUserDetails.getMemberId();
         if (!memberId.equals(itemDto.getMemberId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("수정 권한이 없습니다.");
        }
        try {
            adminItemService.update(itemDto);
         } catch (RuntimeException e) {
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("아이템 수정 실패: " + e.getMessage());
         }
         
        return ResponseEntity.ok("UPDATE DONE");     

    }                              
                                        
//     @PutMapping("/update/{id}")
//     public ResponseEntity<?> updateItem(
//                             @PathVariable("id") Long id, 
//                             @ModelAttribute ItemDto itemDto, 
//                             @AuthenticationPrincipal MyUserDetails myUserDetails) {

//     try {
//         Long currentMemberId = myUserDetails.getMemberId();
        
//         if (itemDto.getMemberId() == null || !currentMemberId.equals(itemDto.getMemberId())) {
//              // 소유자 ID가 없거나, 로그인된 사용자 ID와 불일치할 경우
//              return ResponseEntity.status(HttpStatus.FORBIDDEN).body("수정 권한이 없습니다.");
//         }
    
//         ItemDto resultDto = adminItemService.updateItem(id, itemDto, itemDto.getFileUrl());

//         // 3. 성공 응답
//         return ResponseEntity.ok(resultDto);

//     } catch (RuntimeException e) {
//         // Item을 찾을 수 없거나 S3 처리 중 오류가 발생한 경우
//         System.err.println("아이템 수정 중 오류 발생: " + e.getMessage());
        
//         if (e.getMessage() != null && e.getMessage().contains("Item이 존재하지 않습니다")) {
//             return ResponseEntity.status(HttpStatus.NOT_FOUND).body("수정할 아이템을 찾을 수 없습니다.");
//         }
        
//         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("아이템 수정 실패.");
//     }
// }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteItem(@PathVariable("id") Long id) {
        adminItemService.deleteItem(id);
        return ResponseEntity.ok("관리자 상품 삭제 완료");
    }

    // @DeleteMapping("/delete/{itemId}/image")
    // public ResponseEntity<String> deleteImage(@PathVariable("itemId") Long id) {
    // adminItemService.deleteImage(id);
    // return ResponseEntity.ok("이미지 삭제 완료");
    // }
}
