package org.spring.backendspring.crew.crewJoin.controller;

import lombok.RequiredArgsConstructor;
import org.spring.backendspring.crew.crew.service.CrewService;
import org.spring.backendspring.crew.crewJoin.dto.CrewJoinRequestDto;
import org.spring.backendspring.crew.crewJoin.service.CrewJoinRequestService;
import org.spring.backendspring.crew.crewMember.service.CrewMemberService;
import org.spring.backendspring.crew.crewRun.service.CrewRunMemberService;
import org.spring.backendspring.crew.crewRun.service.CrewRunService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/crew/{crewId}/joinRequest")
public class MyCrewJoinRequestController {

    private final CrewJoinRequestService crewJoinRequestService;


    //해당 크루 가입신청한 멤버
    @GetMapping({"","/"})
    public ResponseEntity<?> myCrewJoinRequestList(@PathVariable("crewId") Long crewId){
        List<CrewJoinRequestDto> myCrewJoinRequestDtoList = crewJoinRequestService.myCrewJoinList(crewId);
        Map<String, Object> myCrewjoinRequestMap = new HashMap<>();
        myCrewjoinRequestMap.put("myCrewJoinList", myCrewJoinRequestDtoList);
        return ResponseEntity.status(HttpStatus.OK).body(myCrewjoinRequestMap);
    }

    //크루 가입 승인
    @PostMapping("/approved")
    public ResponseEntity<?> myCrewJoinRequestApproved(
//            @PathVariable("crewId") Long crewId,
                                               @RequestBody CrewJoinRequestDto joinDto){
        crewJoinRequestService.crewJoinRequestApproved(joinDto);
        return ResponseEntity.ok("승인 완료");
    }

    //크루 가입 승인거절
    @PostMapping("/rejected")
    public ResponseEntity<?> myCrewJoinRequestRejected(
//            @PathVariable("crewId") Long crewId,
                                               @RequestBody CrewJoinRequestDto joinDto){
        crewJoinRequestService.crewJoinRequestRejected(joinDto);
        return ResponseEntity.ok("거절 완료");
    }

}
