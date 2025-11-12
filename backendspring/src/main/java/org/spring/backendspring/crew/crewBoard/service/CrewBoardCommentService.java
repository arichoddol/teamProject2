package org.spring.backendspring.crew.crewBoard.service;

import java.util.List;

import org.spring.backendspring.crew.crewBoard.dto.CrewBoardCommentDto;

public interface CrewBoardCommentService {

    CrewBoardCommentDto writeComment(CrewBoardCommentDto commentDto);

    List<CrewBoardCommentDto> commentList(Long boardId);

    CrewBoardCommentDto commentDetail(Long id);

    CrewBoardCommentDto updateComment(CrewBoardCommentDto crewBoardCommentDto);

    void deleteComment(Long id);

}
