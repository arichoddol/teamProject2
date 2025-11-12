package org.spring.backendspring.crew.crewRun.service;


import org.spring.backendspring.crew.crewRun.dto.CrewRunMemberDto;

import java.util.List;

public interface CrewRunMemberService {
    List<CrewRunMemberDto> findCrewRunMemberList(Long crewId, Long runId);

    void insertCrewMemberRun(Long runId, Long memberId);

    void deleteCrewMemberRun(Long runId, Long memberId);
}
