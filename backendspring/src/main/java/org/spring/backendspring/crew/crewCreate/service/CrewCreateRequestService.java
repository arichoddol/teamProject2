package org.spring.backendspring.crew.crewCreate.service;

import org.spring.backendspring.crew.crewCreate.dto.CrewCreateRequestDto;

import jakarta.validation.Valid;

public interface CrewCreateRequestService {

    void createRequest(@Valid CrewCreateRequestDto crewCreateRequestDto);

    void rejectRequest(Long requestId);

    void approveRequest(Long requestId);
    
}
