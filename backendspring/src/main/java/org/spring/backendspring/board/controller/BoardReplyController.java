package org.spring.backendspring.board.controller;

import org.spring.backendspring.board.dto.BoardReplyDto;
import org.spring.backendspring.board.service.BoardReplyService;
import org.spring.backendspring.board.service.BoardService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Slf4j
@Log4j2
@RestController
@RequestMapping("/api/reply")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class BoardReplyController {

    private final BoardService boardService;
    private final BoardReplyService boardReplyService;


    // 모든 데이터를 하나의 JSON 객체로 받아옵니다.
    @PostMapping("/addReply")
    public ResponseEntity<?> writeReply( @RequestBody BoardReplyDto boardReplyDto){
    
        
        // >>>BoardReplyDto(id=null, boardId=null, memberId=null, title=null, content=ㅇㅇ, createTime=null, upDateTime=null, boardEntity=null, memberEntity=null)
        System.out.println(">>>" +boardReplyDto);
        boardReplyService.insertReply(boardReplyDto);
        return ResponseEntity.ok("reply Added.");
    }

    @GetMapping("/list/{boardId}")
    public ResponseEntity<Page<BoardReplyDto>> getReplyList(
            @PathVariable("boardId") Long boardId,
            @PageableDefault(
                size = 10, 
                sort = "createTime", 
                direction = Sort.Direction.DESC )
            Pageable pageable){

        Page<BoardReplyDto> replyList = boardReplyService.getReplyPage(boardId, pageable);
        return ResponseEntity.ok(replyList);
        }
    
        
    
}
