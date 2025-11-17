package org.spring.backendspring.crew.crewBoard.service.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.spring.backendspring.crew.crewBoard.dto.CrewBoardCommentDto;
import org.spring.backendspring.crew.crewBoard.entity.CrewBoardCommentEntity;
import org.spring.backendspring.crew.crewBoard.entity.CrewBoardEntity;
import org.spring.backendspring.crew.crewBoard.repository.CrewBoardCommentRepository;
import org.spring.backendspring.crew.crewBoard.repository.CrewBoardRepository;
import org.spring.backendspring.crew.crewBoard.service.CrewBoardCommentService;
import org.spring.backendspring.member.entity.MemberEntity;
import org.spring.backendspring.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CrewBoardCommentServiceImpl implements CrewBoardCommentService {
    
    private final CrewBoardCommentRepository crewBoardCommentRepository;
    private final CrewBoardRepository crewBoardRepository;
    private final MemberRepository memberRepository;

    @Override
    public CrewBoardCommentDto createComment(CrewBoardCommentDto commentDto, Long boardId, Long loginUserId) {
        CrewBoardEntity crewBoardEntity = crewBoardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글"));
        
        MemberEntity memberEntity = memberRepository.findById(loginUserId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원"));

        commentDto.setMemberEntity(memberEntity);

        CrewBoardCommentEntity commentEntity = CrewBoardCommentEntity.toEntityC(commentDto, crewBoardEntity);

        crewBoardCommentRepository.save(commentEntity);

        return CrewBoardCommentDto.toDto(commentEntity);
    }

    @Override
    public List<CrewBoardCommentDto> commentList(Long crewId, Long boardId) {
        
        CrewBoardEntity boardEntity = crewBoardRepository.findByCrewEntity_IdAndId(crewId, boardId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글"));
        
        List<CrewBoardCommentEntity> commentEntityList = crewBoardCommentRepository.findAllByCrewBoardEntityOrderByIdDesc(boardEntity);

        return commentEntityList.stream()
            .map(CrewBoardCommentDto::toDto)
            .collect(Collectors.toList());
    }

    @Override
    public CrewBoardCommentDto commentDetail(Long id, Long crewId, Long boardId) {
        return CrewBoardCommentDto.toDto(crewBoardCommentRepository.findByCrew_IdAndBoard_IdAndId(crewId, boardId, id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글")));
    }

    @Override
    public CrewBoardCommentDto updateComment(CrewBoardCommentDto crewBoardCommentDto, Long loginUserId) {
        
        CrewBoardCommentEntity comment = crewBoardCommentRepository.findById(crewBoardCommentDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글"));

        if (!comment.getMemberEntity().getId().equals(loginUserId)) {
            throw new IllegalArgumentException("댓글 작성자만 수정 가능");
        }

        comment.setContent(crewBoardCommentDto.getContent());

        CrewBoardCommentEntity updatedComment  = crewBoardCommentRepository.save(comment);

        return CrewBoardCommentDto.toDto(updatedComment);
    }

    @Override
    public void deleteComment(Long id, Long loginUserId) {
        CrewBoardCommentEntity commentEntity = crewBoardCommentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글 삭제 불가"));

        if (!commentEntity.getMemberEntity().getId().equals(loginUserId)) {
            throw new IllegalArgumentException("댓글 작성자만 삭제 가능");
        }

        crewBoardCommentRepository.delete(commentEntity);
    }
    
}
