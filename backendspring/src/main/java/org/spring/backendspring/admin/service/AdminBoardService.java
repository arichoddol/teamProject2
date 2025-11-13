package org.spring.backendspring.admin.service;

import org.spring.backendspring.board.dto.BoardDto;
import org.spring.backendspring.common.dto.PagedResponse;

public interface AdminBoardService {

    void deleteBoardByAdmin(Long id);

    PagedResponse<BoardDto> findAllBoards(String keyword, int page, int size);

}
