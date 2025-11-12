package org.spring.backendspring.crew.crewBoard.controller;

import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.spring.backendspring.crew.crewBoard.dto.CrewBoardCommentDto;
import org.spring.backendspring.crew.crewBoard.service.CrewBoardCommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@RequestMapping("/api/crewBoardComment")
@RequiredArgsConstructor
public class CrewBoardCommentController {
    
    private final CrewBoardCommentService crewBoardCommentService;

    @PostMapping("/write")
    public ResponseEntity<?> writeComment(@RequestBody CrewBoardCommentDto commentDto) {
        
        CrewBoardCommentDto writeComment = crewBoardCommentService.writeComment(commentDto);

        return ResponseEntity.ok(writeComment);
    }

    @GetMapping("/list/{boardId}")
    public ResponseEntity<?> commentList(@PathVariable("boardId")Long boardId) {
        
        List<CrewBoardCommentDto> crewBoardCommentList = crewBoardCommentService.commentList(boardId);

        Map<String, Object> commentList = new HashMap<>();

        commentList.put("commentList", commentList);

        return ResponseEntity.ok(commentList);
    }
    
    @GetMapping("/detail/{id}")
    public ResponseEntity<?> commentDetail(@PathVariable("id") Long id) {
        
        CrewBoardCommentDto commentDto = crewBoardCommentService.commentDetail(id);

        Map<String, CrewBoardCommentDto> response = new HashMap<>();

        response.put("commentDetail", commentDto);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateComment(@RequestBody CrewBoardCommentDto crewBoardCommentDto) {
        CrewBoardCommentDto updateCommentDto = crewBoardCommentService.updateComment(crewBoardCommentDto);

        Map<String, CrewBoardCommentDto> response = new HashMap<>();

        response.put("updateComment", updateCommentDto);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable("id") Long id) {

        crewBoardCommentService.deleteComment(id);

        return ResponseEntity.ok("댓글 삭제 완료");
    }
}
