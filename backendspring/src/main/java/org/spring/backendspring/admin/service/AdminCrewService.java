package org.spring.backendspring.admin.service;

import java.util.List;

import org.spring.backendspring.crew.crew.dto.CrewDto;

public interface AdminCrewService{
    
    List<CrewDto> findAllCrew();

    CrewDto findCrew(Long crewId);

    void deleteCrewByAdmin(Long id);
}
