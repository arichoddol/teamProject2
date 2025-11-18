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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

// RestController is Data Only
//@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
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
                          Model model) {

        model.addAttribute("myUserDetails", myUserDetails);
        return "board/write";
    }

    // this Fragments -> form action="/board/write" Data request.
    @PostMapping("/write")
    public ResponseEntity<?> writeBoard(@RequestParam("memberId") Long memberId,
                                        @ModelAttribute BoardDto boardDto,
                                        @AuthenticationPrincipal MyUserDetails myUserDetails) throws IOException {
        if (!myUserDetails.getMemberId().equals(memberId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("회원의 정보가 일치하지 않습니다.");
        }
        boardService.insertBoard(boardDto);
        return ResponseEntity.ok("DONE");
    }

    // URL: http://localhost:8088/api/board/detail/{id}
    @GetMapping("/detail/{id}")
    public ResponseEntity<BoardDto> getBoardDetail(@PathVariable("id") Long id) {

        // Bring Id(Long id )
        BoardDto boardDto = boardService.boardDetail(id);

        return ResponseEntity.ok(boardDto);
    }

    // UPDATE
    @GetMapping("/update/{boardId}")
    public ResponseEntity<?> updateBoard(@PathVariable("boardId") Long boardId,
                                         @AuthenticationPrincipal MyUserDetails myUserDetails) {
        Long memberId = myUserDetails.getMemberId();

        try {
            BoardDto boardDto = boardService.boardDetail(boardId);

            if (!memberId.equals(boardDto.getMemberId())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("수정 권한이 없습니다.");
            }

            if (boardDto != null) {
                return ResponseEntity.ok(boardDto);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @PostMapping("/updatePost")
    public ResponseEntity<?> updateBoard(@ModelAttribute BoardDto boardDto,
                                         @AuthenticationPrincipal MyUserDetails myUserDetails) throws IOException {
        Long memberId = myUserDetails.getMemberId();

        if (!memberId.equals(boardDto.getMemberId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("수정 권한이 없습니다.");
        }

        boardService.update(boardDto);
        return ResponseEntity.ok("UPDATE DONE");

    }
    // UPDATE


    //* DELETE 요청: /board/{boardId}
    //* @param boardId 삭제할 게시글 ID
    //* @return 성공 응답
    // DELETE
    @DeleteMapping("/detail/{boardId}")
    public ResponseEntity<String> deleteBoard(@PathVariable("boardId") Long boardId,
                                              @AuthenticationPrincipal MyUserDetails myUserDetails) {
        Long authMemberId = myUserDetails.getMemberId();
        Long memberId = boardService.boardDetail(boardId).getMemberId();

        if (!authMemberId.equals(memberId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("삭제 권한이 없습니다.");
        }

        try {
            boardService.deleteBoard(boardId);
            return ResponseEntity.ok("게시글 삭제 성공");
        } catch (IllegalArgumentException e) {
            // TODO: handle exception
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            // etc 500 error
            return ResponseEntity.internalServerError().body("게시글 삭제중 서버 오류 발생");
        }
    }
}