package org.spring.backendspring;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.spring.backendspring.member.entity.MemberEntity;
import org.spring.backendspring.member.repository.MemberRepository;
import org.spring.backendspring.crew.crew.entity.CrewEntity;
import org.spring.backendspring.crew.crewBoard.dto.CrewBoardDto;
import org.spring.backendspring.crew.crewBoard.repository.CrewBoardRepository;
import org.spring.backendspring.crew.crewBoard.service.CrewBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CrewBoardTest {

    @Autowired
    private CrewBoardService crewBoardService;

    @Autowired
    private CrewBoardRepository crewBoardRepository;
    @Autowired
    private MemberRepository memberRepository;

    @Test
    void createBoard() throws IOException {

        MemberEntity member = memberRepository.findById(2L).orElseThrow(IllegalArgumentException::new);

        CrewEntity crew = CrewEntity.builder()
        .id(1L)  // 실제 DB에 존재하는 크루 ID
        .build();

        CrewBoardDto crewBoardDto = CrewBoardDto.builder()
            .id(5L)
            .title("테스트")
            .content("내용")
            .memberEntity(member)
            .crewEntity(crew)
            .build();

        CrewBoardDto createdBoard = crewBoardService.createBoard(crewBoardDto);
        
        System.out.println("생성된 게시글 ID: " + createdBoard.getId());
        System.out.println("생성된 게시글 제목: " + createdBoard.getTitle());
        
    }
}