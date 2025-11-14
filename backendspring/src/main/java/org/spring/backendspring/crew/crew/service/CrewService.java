package org.spring.backendspring.crew.crew.service;

import java.io.IOException;
import java.util.List;

import org.spring.backendspring.crew.crew.dto.CrewDto;
import org.springframework.web.multipart.MultipartFile;

public interface CrewService {

    CrewDto updateCrew(Long id, CrewDto crewDto, List<MultipartFile> newImages, List<Long> deleteImageId) throws IOException;

    void deleteCrew(Long id);

    List<CrewDto> crewList();

    CrewDto crewDetail(Long id);

    CrewDto findMyCrew(Long crewId, Long memberId);

    List<CrewDto> findAllCrew();
    
}