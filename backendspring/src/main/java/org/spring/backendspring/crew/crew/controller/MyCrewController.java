package org.spring.backendspring.crew.crew.controller;

import lombok.RequiredArgsConstructor;
import org.spring.backendspring.crew.crewJoin.dto.CrewJoinRequestDto;
import org.spring.backendspring.crew.crewJoin.service.CrewJoinRequestService;
import org.spring.backendspring.crew.crewMember.dto.CrewMemberDto;
import org.spring.backendspring.crew.crewMember.service.CrewMemberService;
import org.spring.backendspring.crew.crew.dto.CrewDto;
import org.spring.backendspring.crew.crew.service.CrewService;
import org.spring.backendspring.crew.crewRun.dto.CrewRunDto;
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
@RequestMapping("/api/crew/{crewId}")
public class MyCrewController {

    private final CrewService crewService;
    private final CrewMemberService crewMemberService;

    //내 크루 접속 시 기본
    @GetMapping({"","/"})
    public ResponseEntity<?> myCrew(@PathVariable("crewId") Long crewId){
        CrewDto crewDto = crewService.findCrew(crewId);
        Map<String, Object> myCrew = new HashMap<>();
        myCrew.put("crew", crewDto);
        return ResponseEntity.status(HttpStatus.OK).body(myCrew);
    }

    //크루원 리스트
    @GetMapping("/crewMemberList")
    public ResponseEntity<?> CrewMemberList(@PathVariable("crewId") Long crewId){
        List<CrewMemberDto> crewMemberDtoList = crewMemberService.findCrewMemberList(crewId);
        Map<String, Object> crewMemberListMap = new HashMap<>();
        crewMemberListMap.put("crewMember", crewMemberDtoList);
        return ResponseEntity.status(HttpStatus.OK).body(crewMemberListMap);
    }

    //크루원 상세보기
    @GetMapping("detail/{crewMemberId}")
    public ResponseEntity<?> crewMemberDetails(@PathVariable("crewId") Long crewId,
                                               @PathVariable("crewMemberId") Long crewMemberId){
        CrewMemberDto crewMemberDto = crewMemberService.detailCrewMember(crewId,crewMemberId);
        Map<String, Object> crewMemberDetail = new HashMap<>();
        crewMemberDetail.put("crewMember", crewMemberDto);
        return ResponseEntity.status(HttpStatus.OK).body(crewMemberDetail);
    }

}
