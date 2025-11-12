package org.spring.backendspring.crew.crew.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.spring.backendspring.crew.crew.dto.CrewDto;
import org.spring.backendspring.crew.crew.service.CrewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.PutMapping;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@RestController
@RequestMapping("/api/crew")
@RequiredArgsConstructor
public class CrewController {

    private final CrewService crewService;

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id,
                                    @ModelAttribute CrewDto crewDto,
                                    @RequestParam(value = "newImages", required = false) List<MultipartFile> newImages,
                                    @RequestParam(value = "deleteImageId", required = false) List<Long> deleteImageId) throws IOException {
    
        CrewDto updated = crewService.updateCrew(id, crewDto, newImages, deleteImageId);

        Map<String, CrewDto> response = new HashMap<>();

        response.put("updatedCrew", updated);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {

        crewService.deleteCrew(id);

        return ResponseEntity.ok(Map.of("message", "크루 삭제 완료"));
    }

    @GetMapping("/list")
    public ResponseEntity<?> list() {

        List<CrewDto> crewList = crewService.crewList();
        
        Map<String, Object> response = new HashMap<>();

        response.put("crewList", crewList);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> detail(@PathVariable("id") Long id) {

        CrewDto crewDetail = crewService.crewDetail(id);

        Map<String, CrewDto> response = new HashMap<>();

        response.put("crewDetail", crewDetail);

        return ResponseEntity.ok(response);
    }
    
    
}
