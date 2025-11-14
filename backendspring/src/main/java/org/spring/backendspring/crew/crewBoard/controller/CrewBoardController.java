package org.spring.backendspring.crew.crewBoard.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.spring.backendspring.config.security.MyUserDetails;
import org.spring.backendspring.crew.crewBoard.dto.CrewBoardDto;
import org.spring.backendspring.crew.crewBoard.service.CrewBoardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
                                         @RequestBody CrewBoardDto crewBoardDto,
                                         @AuthenticationPrincipal MyUserDetails userDetails) throws IOException {
        Long loginUserId = userDetails.getMemberId();
        CrewBoardDto createBoard = crewBoardService.createBoard(crewId, crewBoardDto, loginUserId);
        
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
                                         @ModelAttribute CrewBoardDto crewBoardDto,
                                         @RequestParam(value = "newImages", required = false) List<MultipartFile> newImages,
                                         @RequestParam(value = "deleteImageId", required = false) List<Long> deleteImageId,
                                         @AuthenticationPrincipal MyUserDetails userDetails) throws IOException {
        Long loginUserId = userDetails.getMemberId();

        CrewBoardDto updateBoardDto = crewBoardService.updateBoard(id, crewId, crewBoardDto, loginUserId, newImages, deleteImageId);

        Map<String, CrewBoardDto> response = new HashMap<>();

        response.put("updatedBoard", updateBoardDto);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{crewBoardId}")
    public ResponseEntity<?> deleteBoard(@PathVariable("crewBoardId") Long id,
                                         @PathVariable("crewId") Long crewId,
                                         @AuthenticationPrincipal MyUserDetails userDetails) {
        
        Long loginUserId = userDetails.getMemberId();                         
        crewBoardService.deleteBoard(id, loginUserId);

        return ResponseEntity.ok("게시글 삭제 완료");
    }
    
}
