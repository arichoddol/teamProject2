package org.spring.backendspring.admin.controller;

import org.spring.backendspring.admin.service.AdminBoardService;
import org.spring.backendspring.board.dto.BoardDto;
import org.spring.backendspring.board.service.BoardService;
import org.spring.backendspring.common.dto.PagedResponse;
import org.spring.backendspring.member.dto.MemberDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/board")
@RequiredArgsConstructor
public class AdminBoardController {

    private final AdminBoardService adminBoardService;
    private final BoardService boardService;

    // 공통 BasicPagingDto 클래스 만들어서 사용하는 방향으로
    @GetMapping("/boardList")
    public ResponseEntity<PagedResponse<BoardDto>> getAllBoards(
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {

        PagedResponse<BoardDto> boardList = adminBoardService.findAllBoards(keyword, page, size);
        return ResponseEntity.ok(boardList);
    }

    @DeleteMapping("/delete/{boardid}")
    public ResponseEntity<String> deleteBoard(@PathVariable("boardid") Long id) {
        adminBoardService.deleteBoardByAdmin(id);
        return ResponseEntity.ok("관리자에 의해 게시물이 삭제 되었습니다");
    }
}
