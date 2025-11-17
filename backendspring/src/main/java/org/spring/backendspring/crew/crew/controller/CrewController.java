package org.spring.backendspring.crew.crew.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.spring.backendspring.crew.crew.dto.CrewDto;
import org.spring.backendspring.crew.crew.service.CrewService;
import org.spring.backendspring.crew.crewJoin.dto.CrewJoinRequestDto;
import org.spring.backendspring.crew.crewJoin.service.CrewJoinRequestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@RestController
@RequestMapping("/api/crew")
@RequiredArgsConstructor
public class CrewController {

    private final CrewService crewService;

    @PutMapping("/update/{crewId}")
    public ResponseEntity<?> update(@PathVariable("crewId") Long crewId,
                                    @ModelAttribute CrewDto crewDto,
                                    @RequestParam(value = "newImages", required = false) List<MultipartFile> newImages,
                                    @RequestParam(value = "deleteImageId", required = false) List<Long> deleteImageId) throws IOException {
    
        CrewDto updated = crewService.updateCrew(crewId, crewDto, newImages, deleteImageId);

        Map<String, CrewDto> response = new HashMap<>();

        response.put("updatedCrew", updated);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{crewId}")
    public ResponseEntity<?> delete(@PathVariable("crewId") Long crewId) {

        crewService.deleteCrew(crewId);

        return ResponseEntity.ok(Map.of("message", "크루 삭제 완료"));
    }

    @GetMapping("/list")
    public ResponseEntity<?> list() {

        List<CrewDto> crewList = crewService.crewList();
        
        Map<String, Object> response = new HashMap<>();

        response.put("crewList", crewList);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/detail/{crewId}")
    public ResponseEntity<?> detail(@PathVariable("crewId") Long crewId) {

        CrewDto crewDetail = crewService.crewDetail(crewId);

        Map<String, CrewDto> response = new HashMap<>();

        response.put("crewDetail", crewDetail);

        return ResponseEntity.ok(response);
    }

    //CrewsController 없애면서 CrewController 로 옮겨주세요 axios 프론트 주소도,,ㅎㅎ
    private final CrewJoinRequestService crewJoinRequestService;
    @PostMapping("/joinRequest")
    public ResponseEntity<?> crewJoinRequests(
            @RequestBody CrewJoinRequestDto joinDto){
        CrewJoinRequestDto crewJoinRequestDto = crewJoinRequestService.crewJoinRequest(joinDto);
        Map<String, Object> joinMap = new HashMap<>();
        joinMap.put("joinRequest", crewJoinRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(joinMap);
    }
    
    
}
