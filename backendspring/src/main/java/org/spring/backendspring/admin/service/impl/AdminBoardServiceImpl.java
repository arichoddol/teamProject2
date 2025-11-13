package org.spring.backendspring.admin.service.impl;

import org.spring.backendspring.admin.repository.AdminBoardRepository;
import org.spring.backendspring.admin.service.AdminBoardService;
import org.spring.backendspring.board.dto.BoardDto;
import org.spring.backendspring.board.entity.BoardEntity;
import org.spring.backendspring.board.repository.BoardRepository;
import org.spring.backendspring.common.dto.PagedResponse;
import org.spring.backendspring.member.dto.MemberDto;
import org.spring.backendspring.member.entity.MemberEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminBoardServiceImpl implements AdminBoardService {

    private final BoardRepository boardRepository;
    private final AdminBoardRepository adminBoardRepository;

    // List 는 공통 BasicPagingDto 클래스 만들어서 사용하는 방향으로 ()
    @Override
    public PagedResponse<BoardDto> findAllBoards(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<BoardDto> boardPage;

        if (keyword == null || keyword.trim().isEmpty()) {
            boardPage = boardRepository.findAll(pageable)
                    .map(BoardDto::toBoardDto);
        } else {
            boardPage = adminBoardRepository
                    .findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(keyword, keyword, pageable)
                    .map(BoardDto::toBoardDto);
        }

        return PagedResponse.of(boardPage);
    }

    @Override
    public void deleteBoardByAdmin(Long id) {
        BoardEntity board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시물을 찾을 수 없습니다."));
        boardRepository.delete(board);
    }
}
