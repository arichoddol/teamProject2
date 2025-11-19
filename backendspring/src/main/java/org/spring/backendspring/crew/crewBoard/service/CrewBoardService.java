package org.spring.backendspring.crew.crewBoard.service;

import java.io.IOException;
import java.util.List;

import org.spring.backendspring.common.dto.PagedResponse;
import org.spring.backendspring.crew.crewBoard.dto.CrewBoardDto;
import org.springframework.web.multipart.MultipartFile;

public interface CrewBoardService {

    CrewBoardDto createBoard(Long crewId, CrewBoardDto crewBoardDto, Long loginUserId) throws IOException;
        
    CrewBoardDto boardDetail(Long crewId, Long id);
    
    CrewBoardDto updateBoard(Long id, Long crewId, CrewBoardDto crewBoardDto, Long loginUserId, List<MultipartFile> newImages, List<Long> deleteImageId) throws IOException;
    
    void deleteBoard(Long id, Long crewId, Long loginUserId);

    PagedResponse<CrewBoardDto> boardListByCrew(Long crewId, String keyword, int page, int size);
    
}
