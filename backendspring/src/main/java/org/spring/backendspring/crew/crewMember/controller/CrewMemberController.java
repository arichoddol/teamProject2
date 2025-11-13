package org.spring.backendspring.crew.crewMember.controller;

import lombok.RequiredArgsConstructor;
import org.spring.backendspring.crew.crewMember.dto.CrewMemberDto;
import org.spring.backendspring.crew.crewMember.service.CrewMemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mycrew/{crewId}/member")
public class CrewMemberController {

    private final CrewMemberService crewMemberService;

  

    //크루원 리스트
    @GetMapping({"","/"})
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

    //크루원 삭제
    @GetMapping("delete/{crewMemberTbId}")
    public ResponseEntity<?> crewMemberDelete(@PathVariable("crewId") Long crewId,
                                               @PathVariable("crewMemberId") Long crewMemberTbId){
        crewMemberService.deleteCrewMember(crewMemberTbId);
        return ResponseEntity.ok("크루원삭제완료");
    }
}
