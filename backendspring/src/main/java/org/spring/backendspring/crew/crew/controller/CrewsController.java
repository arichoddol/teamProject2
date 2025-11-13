package org.spring.backendspring.crew.crew.controller;

import lombok.RequiredArgsConstructor;
import org.spring.backendspring.crew.crewJoin.dto.CrewJoinRequestDto;
import org.spring.backendspring.crew.crewJoin.service.CrewJoinRequestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/crews")
public class CrewsController {

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
