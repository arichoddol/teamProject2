package org.spring.backendspring.item.controller;
import org.spring.backendspring.board.controller.BoardController;
import org.spring.backendspring.board.dto.BoardDto;
import org.spring.backendspring.item.dto.ItemDto;
import org.spring.backendspring.item.service.ItemService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/shop")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000" )
public class ItemController {

    private final ItemService itemService;


    @GetMapping("")
    public ResponseEntity<Page<ItemDto>> itemList(@PageableDefault(size = 10) Pageable pageable){

        Page<ItemDto> itemList = itemService.pagingSearchItemList(pageable, null, null);

        return ResponseEntity.ok(itemList);
        // temp

    }

    @GetMapping("/search")
    public ResponseEntity<Page<ItemDto>> itemSearchList(
        @PageableDefault(size = 10) Pageable pageable,
        @RequestParam(required = false) String subject, 
        @RequestParam(required =  false) String search) {

            Page<ItemDto> itemList = itemService.pagingSearchItemList(pageable, subject, search);
            return ResponseEntity.ok(itemList);
        }
    
    
}
