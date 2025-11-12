package org.spring.backendspring.crew.crewBoard.service.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.spring.backendspring.crew.crewBoard.dto.CrewBoardCommentDto;
import org.spring.backendspring.crew.crewBoard.entity.CrewBoardCommentEntity;
import org.spring.backendspring.crew.crewBoard.entity.CrewBoardEntity;
import org.spring.backendspring.crew.crewBoard.repository.CrewBoardCommentRepository;
import org.spring.backendspring.crew.crewBoard.repository.CrewBoardRepository;
import org.spring.backendspring.crew.crewBoard.service.CrewBoardCommentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CrewBoardCommentServiceImpl implements CrewBoardCommentService {
    
    private final CrewBoardCommentRepository crewBoardCommentRepository;
    private final CrewBoardRepository crewBoardRepository;

    @Override
    public CrewBoardCommentDto writeComment(CrewBoardCommentDto commentDto) {
        CrewBoardEntity crewBoardEntity = crewBoardRepository.findById(commentDto.getCrewBoardEntity().getId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글"));
        
        if (crewBoardEntity != null) {
            CrewBoardCommentEntity commentEntity = CrewBoardCommentEntity.toEntityC(commentDto, crewBoardEntity);

            crewBoardCommentRepository.save(commentEntity);

            return CrewBoardCommentDto.toDto(commentEntity);
        }

        return null;
    }

    @Override
    public List<CrewBoardCommentDto> commentList(Long boardId) {
        
        CrewBoardEntity boardEntity = crewBoardRepository.findById((boardId))
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글"));
        
        List<CrewBoardCommentEntity> commentEntityList = crewBoardCommentRepository.findAllByCrewBoardEntityOrderByIdDesc(boardEntity);

        List<CrewBoardCommentDto> commentList = new ArrayList<>();

        for (CrewBoardCommentEntity commentEntity : commentEntityList) {
            CrewBoardCommentDto commentDto = CrewBoardCommentDto.toDto(commentEntity);
            commentList.add(commentDto);
        }
        return commentList;
    }

    @Override
    public CrewBoardCommentDto commentDetail(Long id) {
        return CrewBoardCommentDto.toDto(crewBoardCommentRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new));
    }

    @Override
    public CrewBoardCommentDto updateComment(CrewBoardCommentDto crewBoardCommentDto) {
        crewBoardCommentRepository.findById(crewBoardCommentDto.getId())
                .orElseThrow(IllegalArgumentException::new);

        CrewBoardEntity crewBoardEntity = CrewBoardEntity.builder()
                                            .id(crewBoardCommentDto.getBoardId())
                                            .build();
        
        crewBoardCommentDto.setCrewBoardEntity(crewBoardEntity);
        CrewBoardCommentEntity commentEntity = crewBoardCommentRepository.save(CrewBoardCommentEntity.toEntity(crewBoardCommentDto));

        return CrewBoardCommentDto.toDto(commentEntity);
    }

    @Override
    public void deleteComment(Long id) {
        CrewBoardCommentEntity commentEntity = crewBoardCommentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글 삭제 불가"));

        crewBoardCommentRepository.delete(commentEntity);
    }
    
}
