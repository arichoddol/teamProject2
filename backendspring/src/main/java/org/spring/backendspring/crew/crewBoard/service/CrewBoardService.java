package org.spring.backendspring.crew.crewBoard.service;

import java.io.IOException;
import java.util.List;

import org.spring.backendspring.crew.crewBoard.dto.CrewBoardDto;

public interface CrewBoardService {

    CrewBoardDto createBoard(CrewBoardDto crewBoardDto) throws IOException;
    
    List<CrewBoardDto> crewBoardList();
    
    CrewBoardDto boardDetail(Long id);
    
    CrewBoardDto updateBoard(Long id, CrewBoardDto crewBoardDto) throws IOException;
    
    void deleteBoard(Long id);
    
}
