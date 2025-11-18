package org.spring.backendspring.item.controller;

import org.spring.backendspring.item.dto.ItemReplyDto;
import org.spring.backendspring.item.service.ItemReplyService;
import org.spring.backendspring.item.service.ItemService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/itemReply")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class ItemReplyController {

    private final ItemService itemService;
    private final ItemReplyService itemReplyService;

    // All Data return OneJSON type 
    @PostMapping("/addReply")
    public ResponseEntity<?> writeReply(@RequestBody ItemReplyDto itemReplyDto){

        System.out.println(">>>"+itemReplyDto);
        itemReplyService.insertReply(itemReplyDto);
        return ResponseEntity.ok("reply Added");
    }

    @GetMapping("list/{itemId}")
    public ResponseEntity<Page<ItemReplyDto>> getReplyList(
        @PathVariable("itemId") Long itemId,
        @PageableDefault(
                size = 10, 
                sort = "createTime", 
                direction = Sort.Direction.DESC ) Pageable pageable
        ){
            Page<ItemReplyDto> replyList = itemReplyService.getReplyPage(itemId, pageable);
            return ResponseEntity.ok(replyList);

        }  
}
