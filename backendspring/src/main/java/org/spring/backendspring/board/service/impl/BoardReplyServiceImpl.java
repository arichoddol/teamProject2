package org.spring.backendspring.board.service.impl;

import java.util.Optional;

import org.spring.backendspring.board.dto.BoardReplyDto;
import org.spring.backendspring.board.entity.BoardEntity;
import org.spring.backendspring.board.entity.BoardReplyEntity;
import org.spring.backendspring.board.repository.BoardReplyRepository;
import org.spring.backendspring.board.repository.BoardRepository;
import org.spring.backendspring.board.service.BoardReplyService;
import org.spring.backendspring.member.entity.MemberEntity;
import org.spring.backendspring.member.repository.MemberRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardReplyServiceImpl implements BoardReplyService{

    private final BoardRepository boardRepository;
    private final BoardReplyRepository boardReplyRepository;
    private final MemberRepository memberRepository;

    @Override
    public Long insertReply(BoardReplyDto boardReplyDto) {

        // boardId check 
        Optional<BoardEntity> optinalBoardEntity 
            = boardRepository.findById(boardReplyDto.getBoardId());
        if(optinalBoardEntity.isPresent()){
            BoardEntity boardEntity = optinalBoardEntity.get();
            boardReplyDto.setBoardEntity(boardEntity);

        // member Check 
        if(boardReplyDto.getMemberId() == null) {
            throw new IllegalArgumentException("존재하지 않는 회원ID");
        }
        Optional<MemberEntity> optionalMemberEntity 
                = memberRepository.findById(boardReplyDto.getMemberId());
        if(!optionalMemberEntity.isPresent()){
            throw new IllegalArgumentException("존재하지 않는 회원 ID입니다");
        }
            
        // 찾은 MemberEntity를 DTO에 설정 (BoardReplyEntity로 변환 시 사용됨)
        boardReplyDto.setMemberEntity(optionalMemberEntity.get());


        // Entitiy Change& SAVE 
        BoardReplyEntity boardReplyEntity = 
                BoardReplyEntity.toReplyEntity(boardReplyDto);
        return boardReplyRepository.save(boardReplyEntity).getId();
    
    }
    return null;
    }

    @Override
    public Page<BoardReplyDto> getReplyPage(Long boardId, Pageable pageable) {
        
        // 1. Page<BoardReplyEntity> 조회
        Page<BoardReplyEntity> replyEntitiesPage = 
        boardReplyRepository.findAllByBoardEntity_Id(boardId, pageable);

        // 2. Entity Page를 DTO Page로 변환
    // map() 함수를 사용하면 Page의 메타데이터(총 페이지 수, 총 개수 등)가 자동으로 유지됩니다.
        Page<BoardReplyDto> replyDtoPage = replyEntitiesPage
            .map(BoardReplyDto::tBoardReplyDto); 
            // 💡 BoardReplyEntity에 정의된 tBoardReplyDto 변환 함수 사용

            return replyDtoPage;
    } 
}
