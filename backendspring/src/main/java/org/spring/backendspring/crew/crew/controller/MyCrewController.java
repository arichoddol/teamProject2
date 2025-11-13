package org.spring.backendspring.crew.crew.controller;

import lombok.RequiredArgsConstructor;
import org.spring.backendspring.crew.crew.dto.CrewDto;
import org.spring.backendspring.crew.crew.service.CrewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mycrew/{crewId}")
public class MyCrewController {

    private final CrewService crewService;

    //내 크루 접속 시 기본
    @GetMapping({"","/"})
    public ResponseEntity<?> myCrew(@PathVariable("crewId") Long crewId){
        CrewDto crewDto = crewService.findCrew(crewId);
        Map<String, Object> myCrew = new HashMap<>();
        myCrew.put("crew", crewDto);
        return ResponseEntity.status(HttpStatus.OK).body(myCrew);
    }


}
