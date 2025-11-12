package org.spring.backendspring.admin.controller;

import org.spring.backendspring.admin.service.AdminCrewService;
import org.spring.backendspring.crew.crew.service.CrewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/crew")
public class AdminCrewController {

    private final CrewService crewService;
    private final AdminCrewService adminCrewService;

    @DeleteMapping("/delete/{crewId}")
    public ResponseEntity<String> deleteCrew(@PathVariable("crewId") Long id) {
        adminCrewService.deleteCrewByAdmin(id);
        return ResponseEntity.ok("관리자에 의해 크루가 삭제 되었습니다");
    }
}
