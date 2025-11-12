package org.spring.backendspring.admin.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.spring.backendspring.admin.service.AdminCrewService;
import org.spring.backendspring.crew.crew.dto.CrewDto;
import org.spring.backendspring.crew.crew.entity.CrewEntity;
import org.spring.backendspring.crew.crew.repository.CrewRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminCrewServiceImpl implements AdminCrewService {

    private final CrewRepository crewRepository;

    @Override
    public List<CrewDto> findAllCrew() {
        return crewRepository.findAll().stream().map(CrewDto::toCrewDto).collect(Collectors.toList());
    }

    @Override
    public CrewDto findCrew(Long crewId) {
        return crewRepository.findById(crewId)
                .map(CrewDto::toCrewDto)
                .orElseThrow(() -> new IllegalArgumentException("크루를 찾을 수 없습니다."));
    }

    @Override
    public void deleteCrewByAdmin(Long id) {
        CrewEntity crew = crewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("크루를 찾을 수 없습니다."));
        crewRepository.delete(crew);
    }

}
