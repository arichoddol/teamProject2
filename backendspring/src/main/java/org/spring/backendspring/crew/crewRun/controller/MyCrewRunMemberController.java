package org.spring.backendspring.crew.crewRun.controller;

import lombok.RequiredArgsConstructor;
import org.spring.backendspring.crew.crewRun.dto.CrewRunMemberDto;
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
@RequestMapping("/api/crew/{crewId}/run/{runId}/member")
public class MyCrewRunMemberController {

    private final CrewRunMemberService crewRunMemberService;



    //크루 런닝 스케줄
    @GetMapping({"","/"})
    public ResponseEntity<?> myCrewRunMember(
            @PathVariable("crewId") Long crewId,
            @PathVariable("runId") Long runId){
        List<CrewRunMemberDto> crewRunMemberDtoDtoList =
                crewRunMemberService.findCrewRunMemberList(crewId,runId);
        Map<String,Object> crewRunMemberListMap = new HashMap<>();
        crewRunMemberListMap.put("crewRunMember", crewRunMemberDtoDtoList);
        return ResponseEntity.status(HttpStatus.OK).body(crewRunMemberListMap);
    }

    //크루 런닝 참가
    @PostMapping("/{memberId}/yes")
    public ResponseEntity<?> myCrewRunMemberYes(
//            @PathVariable("crewId") Long crewId,
                                            @PathVariable("runId") Long runId,
                                            @PathVariable("memberId") Long memberId
    ){
        crewRunMemberService.insertCrewMemberRun(runId,memberId);
        return ResponseEntity.ok("참가완료");
    }

    //참가 취소
    @DeleteMapping("/{memberId}/no")
    public ResponseEntity<?> myCrewRunMemberNo(
//            @PathVariable("crewId") Long crewId,
                                           @PathVariable("runId") Long runId,
                                           @PathVariable("memberId") Long memberId){
        crewRunMemberService.deleteCrewMemberRun(runId,memberId);
        return ResponseEntity.ok("참가완료");
    }


    
}
