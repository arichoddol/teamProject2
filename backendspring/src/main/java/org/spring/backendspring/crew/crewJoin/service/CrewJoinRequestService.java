package org.spring.backendspring.crew.crewJoin.service;

import org.spring.backendspring.crew.crewJoin.dto.CrewJoinRequestDto;

import java.util.List;

public interface CrewJoinRequestService {
    CrewJoinRequestDto crewJoinRequest(CrewJoinRequestDto joinDto);

    List<CrewJoinRequestDto> myCrewJoinList(Long crewId);

    void crewJoinRequestApproved(CrewJoinRequestDto joinDto, Long memberId);

    void crewJoinRequestRejected(CrewJoinRequestDto joinDto, Long leaderId);
}
