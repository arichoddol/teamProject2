package org.spring.backendspring.board.service;

import org.spring.backendspring.board.dto.BoardReplyDto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardReplyService {

    Long insertReply(BoardReplyDto boardReplyDto);

    Page<BoardReplyDto> getReplyPage(Long boardId, Pageable pageable);



}
