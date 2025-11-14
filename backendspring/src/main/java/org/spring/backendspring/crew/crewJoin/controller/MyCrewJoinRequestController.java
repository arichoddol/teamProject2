package org.spring.backendspring.crew.crewJoin.controller;

import lombok.RequiredArgsConstructor;
import org.spring.backendspring.common.dto.PagedResponse;
import org.spring.backendspring.config.security.MyUserDetails;
import org.spring.backendspring.crew.crewJoin.dto.CrewJoinRequestDto;
import org.spring.backendspring.crew.crewJoin.service.CrewJoinRequestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mycrew/{crewId}/joinRequest")
public class MyCrewJoinRequestController {

    private final CrewJoinRequestService crewJoinRequestService;


    //해당 크루 가입신청한 멤버
    @GetMapping({"", "/"})
    public ResponseEntity<?> myCrewJoinRequestList(@PathVariable("crewId") Long crewId) {
        List<CrewJoinRequestDto> myCrewJoinRequestDtoList = crewJoinRequestService.myCrewJoinList(crewId);
        Map<String, Object> myCrewjoinRequestMap = new HashMap<>();
        myCrewjoinRequestMap.put("myCrewJoinList", myCrewJoinRequestDtoList);
        return ResponseEntity.status(HttpStatus.OK).body(myCrewjoinRequestMap);
    }
    // @GetMapping({"","/"})
    // public ResponseEntity<PagedResponse<CrewJoinRequestDto>> myCrewJoinRequestList(
    //     @PathVariable("crewId") Long crewId,
    //     @RequestParam(name = "keyword", required = false) String keyword,
    //     @RequestParam(name = "page", defaultValue = "0") int page,
    //     @RequestParam(name = "size", defaultValue = "10") int size){
        
    //     PagedResponse<CrewJoinRequestDto> joinRequestList 
    //     = crewJoinRequestService.pagingJoinReqList(crewId, keyword, page, size);
    //     return ResponseEntity.ok(joinRequestList);
    // }

    //크루 가입 승인
    @PostMapping("/approved")
    public ResponseEntity<?> myCrewJoinRequestApproved(
            //            @PathVariable("crewId") Long crewId,
            @RequestBody CrewJoinRequestDto joinDto,
            @AuthenticationPrincipal MyUserDetails myUserDetails) {
        Long memberId = myUserDetails.getMemberEntity().getId();
        crewJoinRequestService.crewJoinRequestApproved(joinDto, memberId);
        return ResponseEntity.ok("승인 완료");
    }

    //크루 가입 승인거절
    @PostMapping("/rejected")
    public ResponseEntity<?> myCrewJoinRequestRejected(
//            @PathVariable("crewId") Long crewId,
            @RequestBody CrewJoinRequestDto joinDto,
            @AuthenticationPrincipal MyUserDetails myUserDetails) {
        Long memberId = myUserDetails.getMemberEntity().getId();
        crewJoinRequestService.crewJoinRequestRejected(joinDto, memberId);
        return ResponseEntity.ok("거절 완료");
    }

}
