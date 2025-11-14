package org.spring.backendspring.admin.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.spring.backendspring.admin.repository.AdminCrewRepository;
import org.spring.backendspring.admin.service.AdminCrewService;
import org.spring.backendspring.common.dto.PagedResponse;
import org.spring.backendspring.crew.crew.dto.CrewDto;
import org.spring.backendspring.crew.crew.entity.CrewEntity;
import org.spring.backendspring.crew.crew.repository.CrewRepository;
import org.spring.backendspring.member.dto.MemberDto;
import org.spring.backendspring.member.entity.MemberEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminCrewServiceImpl implements AdminCrewService {

    private final CrewRepository crewRepository;
    private final AdminCrewRepository adminCrewRepository;

    @Override
    public List<CrewDto> findAllCrew() {
        return crewRepository.findAll().stream().map(CrewDto::toCrewDto).collect(Collectors.toList());
    }

    @Override
    public CrewDto findCrew(Long crewId) {
        return adminCrewRepository.findById(crewId)
                .map(CrewDto::toCrewDto)
                .orElseThrow(() -> new IllegalArgumentException("크루를 찾을 수 없습니다."));
    }

    @Override
    public PagedResponse<CrewDto> findAllCrews(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<CrewDto> crewPage;

        if (keyword == null || keyword.trim().isEmpty()) {
            // 검색어 없을 때 전체 목록
            crewPage = adminCrewRepository.findAll(pageable)
                    .map(CrewDto::toCrewDto);
        } else {
            // 검색어 있을 때 name 기준으로 검색
            crewPage = adminCrewRepository
                    .findByNameContainingIgnoreCase(keyword, pageable)
                    .map(CrewDto::toCrewDto);
        }

        return PagedResponse.of(crewPage);
    }

    @Override
    public void deleteCrewByAdmin(Long id) {
        CrewEntity crew = crewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("크루를 찾을 수 없습니다."));
        crewRepository.delete(crew);
    }

}
