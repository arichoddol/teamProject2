package org.spring.backendspring.crew.crewBoard.service;

import java.io.IOException;
import java.util.List;

import org.spring.backendspring.crew.crewBoard.dto.CrewBoardDto;

public interface CrewBoardService {

    CrewBoardDto createBoard(Long crewId, CrewBoardDto crewBoardDto) throws IOException;
        
    CrewBoardDto boardDetail(Long crewId, Long id);
    
    CrewBoardDto updateBoard(Long id, Long crewId, CrewBoardDto crewBoardDto) throws IOException;
    
    void deleteBoard(Long id);

    List<CrewBoardDto> boardListByCrew(Long crewId);
    
}
