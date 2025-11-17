package org.spring.backendspring.crew.crewBoard.service;

import java.util.List;

import org.spring.backendspring.crew.crewBoard.dto.CrewBoardCommentDto;

public interface CrewBoardCommentService {

    CrewBoardCommentDto createComment(CrewBoardCommentDto commentDto, Long boardId, Long loginUserId);

    List<CrewBoardCommentDto> commentList(Long crewId, Long boardId);

    CrewBoardCommentDto commentDetail(Long id, Long crewId, Long boardId);

    CrewBoardCommentDto updateComment(CrewBoardCommentDto crewBoardCommentDto, Long loginUserId);

    void deleteComment(Long id, Long loginUserId);

}
