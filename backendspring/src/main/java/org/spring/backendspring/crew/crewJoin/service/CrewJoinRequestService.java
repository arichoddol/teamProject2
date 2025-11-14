package org.spring.backendspring.crew.crewJoin.service;

import org.spring.backendspring.common.dto.PagedResponse;
import org.spring.backendspring.crew.crewJoin.dto.CrewJoinRequestDto;

import java.util.List;

public interface CrewJoinRequestService {
    CrewJoinRequestDto crewJoinRequest(CrewJoinRequestDto joinDto);

    List<CrewJoinRequestDto> myCrewJoinList(Long crewId);

    void crewJoinRequestApproved(CrewJoinRequestDto joinDto);

    void crewJoinRequestRejected(CrewJoinRequestDto joinDto);

    // PagedResponse<CrewJoinRequestDto> pagingJoinReqList(Long crewId, String keyword, int page, int size);
}
