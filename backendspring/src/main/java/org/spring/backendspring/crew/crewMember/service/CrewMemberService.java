package org.spring.backendspring.crew.crewMember.service;

import org.spring.backendspring.crew.crewMember.dto.CrewMemberDto;

import java.util.List;

public interface CrewMemberService {
    List<CrewMemberDto> findCrewMemberList(Long crewId);
    
    void deleteCrewMember(Long crewMemberTbId);

    CrewMemberDto detailCrewMember(Long crewId, Long crewMemberId);
}
