package org.spring.backendspring.crew.crew.controller;

import lombok.RequiredArgsConstructor;
import org.spring.backendspring.crew.crewJoin.dto.CrewJoinRequestDto;
import org.spring.backendspring.crew.crewJoin.service.CrewJoinRequestService;
import org.spring.backendspring.crew.crew.dto.CrewDto;
import org.spring.backendspring.crew.crew.service.CrewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/crews")
public class CrewsController {
    // tmp

    private final CrewJoinRequestService crewJoinRequestService;
    private final CrewService crewService;

    @GetMapping({"","/"})
    public ResponseEntity<?> crewsList(){
        List<CrewDto> crewDtoList = crewService.findAllCrew();
        Map<String, Object> crewList = new HashMap<>();
        crewList.put("crews", crewDtoList);
        return ResponseEntity.status(HttpStatus.OK).body(crewList);
    }

    @GetMapping("detail/{crewId}")
    public ResponseEntity<?> crewDetails(@PathVariable("crewId") Long crewId){
        CrewDto crewDto = crewService.findCrew(crewId);
        Map<String, Object> crewDetail = new HashMap<>();
        crewDetail.put("crew", crewDto);
        return ResponseEntity.status(HttpStatus.OK).body(crewDetail);
    }

    @PostMapping("/joinRequest")
    public ResponseEntity<?> crewJoinRequests(
            @RequestBody CrewJoinRequestDto joinDto){
        CrewJoinRequestDto crewJoinRequestDto = crewJoinRequestService.crewJoinRequest(joinDto);
        Map<String, Object> joinMap = new HashMap<>();
        joinMap.put("joinRequest", crewJoinRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(joinMap);
    }


}
