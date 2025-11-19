package org.spring.backendspring.crew.crew.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.spring.backendspring.config.security.MyUserDetails;
import org.spring.backendspring.crew.crew.dto.CrewDto;
import org.spring.backendspring.crew.crew.service.CrewService;
import org.spring.backendspring.crew.crewMember.dto.CrewMemberDto;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties.Http;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mycrew")
public class MyCrewController {

    private final CrewService crewService;

    @GetMapping("/list")
    public ResponseEntity<?> myCrewList(@AuthenticationPrincipal MyUserDetails myUserDetails) {
        Long memberId = myUserDetails.getMemberId();
        List<CrewMemberDto> myAllCrew = crewService.findMyAllCrew(memberId);
        Map<String, Object> myCrewList = new HashMap<>();
        myCrewList.put("myCrewList", myAllCrew);
        return ResponseEntity.status(HttpStatus.OK).body(myCrewList);
    }

    //내 크루 접속 시 기본
    @GetMapping({"/{crewId}"})
    public ResponseEntity<?> myCrew(@PathVariable("crewId") Long crewId,
                                    @AuthenticationPrincipal MyUserDetails myUserDetails){
        Long memberId = myUserDetails.getMemberEntity().getId();
        CrewDto crewDto = crewService.findMyCrew(crewId, memberId);
        Map<String, Object> myCrew = new HashMap<>();
        myCrew.put("crew", crewDto);
        return ResponseEntity.status(HttpStatus.OK).body(myCrew);
    }
}
