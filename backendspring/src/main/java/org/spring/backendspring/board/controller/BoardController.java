package org.spring.backendspring.board.controller;


import java.io.IOException;


import org.spring.backendspring.board.dto.BoardDto;
import org.spring.backendspring.board.service.BoardService;
import org.spring.backendspring.config.security.MyUserDetails;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

// RestController is Data Only
@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class BoardController {

    private final BoardService boardService;

    @GetMapping("")
    public ResponseEntity<Page<BoardDto>> boardList(@PageableDefault(size = 10) Pageable pageable) {

        Page<BoardDto> boardList = boardService.boardListPage(pageable, null, null);

        return ResponseEntity.ok(boardList);
    }

    // search function
    @GetMapping("/search")
    public ResponseEntity<Page<BoardDto>> boardSearchList(
        @PageableDefault(size = 10) Pageable pageable,
        @RequestParam(required = false) String subject,
        @RequestParam(required = false) String search) {

            Page<BoardDto> boardList = boardService.boardListPage(pageable, subject, search);
            return ResponseEntity.ok(boardList);
        }
    
        // POST can  after Login 
        @GetMapping("/newPost")
        public String newPost(BoardDto boardDto,
                            @AuthenticationPrincipal MyUserDetails myUserDetails,
                            Model model ){

            model.addAttribute("myUserDetails", myUserDetails);
            return "board/write";
        }
        // this Fragments -> form action="/board/write" Data request.


        



    @PostMapping("/write")
    public ResponseEntity<?> writeBoard(@RequestParam("memberId") Long memberId,
                                        @ModelAttribute BoardDto boardDto) throws IOException{
        boardService.insertBoard(boardDto);
      
        return ResponseEntity.ok("DONE");
    }
}


