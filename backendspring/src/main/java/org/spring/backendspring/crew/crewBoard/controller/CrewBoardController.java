package org.spring.backendspring.crew.crewBoard.controller;

import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RequestMapping;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.spring.backendspring.crew.crewBoard.dto.CrewBoardDto;
import org.spring.backendspring.crew.crewBoard.service.CrewBoardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/mycrew/{crewId}/board")
@RequiredArgsConstructor
public class CrewBoardController {
    
    private final CrewBoardService crewBoardService;

    @GetMapping({"", "/", "/list"})
    public ResponseEntity<?> boardListByCrew(@PathVariable("crewId") Long crewId) {

        List<CrewBoardDto> crewBoardDtoList = crewBoardService.boardListByCrew(crewId);

        Map<String, Object> crewBoardList = new HashMap<>();
        
        crewBoardList.put("crewBoardList", crewBoardDtoList);

        return ResponseEntity.ok(crewBoardList);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createBoard(@PathVariable("crewId") Long crewId,
                                         @RequestBody CrewBoardDto crewBoardDto) throws IOException {

        CrewBoardDto createBoard = crewBoardService.createBoard(crewId, crewBoardDto);

        return ResponseEntity.ok(createBoard);
    }

    @GetMapping("/detail/{crewBoardId}")
    public ResponseEntity<?> boardDetail(@PathVariable("crewBoardId") Long id,
                                         @PathVariable("crewId") Long crewId) {

        CrewBoardDto crewBoardDto = crewBoardService.boardDetail(crewId, id);
        
        Map<String, CrewBoardDto> response = new HashMap<>();

        response.put("boardDetail", crewBoardDto);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{crewBoardId}")
    public ResponseEntity<?> updateBoard(@PathVariable("crewBoardId") Long id,
                                         @PathVariable("crewId") Long crewId,
                                         @ModelAttribute CrewBoardDto crewBoardDto) throws IOException {

        CrewBoardDto updateBoardDto = crewBoardService.updateBoard(id, crewId, crewBoardDto);

        Map<String, CrewBoardDto> response = new HashMap<>();

        response.put("updatedBoard", updateBoardDto);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{crewBoardId}")
    public ResponseEntity<?> deleteBoard(@PathVariable("crewBoardId") Long id,
                                         @PathVariable("crewId") Long crewId) {
        
        crewBoardService.deleteBoard(id);

        return ResponseEntity.ok("게시글 삭제 완료");
    }
    
}
