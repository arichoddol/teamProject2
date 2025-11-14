package org.spring.backendspring.board.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.spring.backendspring.board.dto.BoardDto;
import org.spring.backendspring.board.dto.BoardImgDto;
import org.spring.backendspring.board.entity.BoardEntity;
import org.spring.backendspring.board.entity.BoardImgEntity;
import org.spring.backendspring.board.repository.BoardImgRepository;
import org.spring.backendspring.board.repository.BoardRepository;

import org.spring.backendspring.board.service.BoardService;
import org.spring.backendspring.item.entity.ItemImgEntity;
import org.spring.backendspring.member.entity.MemberEntity;
import org.spring.backendspring.member.repository.MemberRepository;
import org.spring.backendspring.s3.AwsS3Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardServiceImpl implements BoardService {

    // S3 bucket
    private final AwsS3Service awsS3Service;

    private final BoardRepository boardRepository;
    private final BoardImgRepository boardImgRepository;
    private final MemberRepository memberRepository;
    String newFileName = "";
    // i guess AWS S3 Bucket Link?
    private static final String FILE_PATH = "C:/full/upload/";


    @Override
    @Transactional
    public void insertBoard(BoardDto boardDto) throws IOException {
        
        BoardImgDto boardImgDto = new BoardImgDto();
        // Membmer Check 
        MemberEntity memberEntity = memberRepository.findById(boardDto.getMemberId())
                    .orElseThrow(IllegalArgumentException::new);
        boardDto.setMemberentity(memberEntity);


        // if file is Empty..
        if(boardDto.getBoardFile().isEmpty()){
           
            boardDto.setAttachFile(0);
            boardDto.setMemberentity(memberEntity);
         
             // DTO -> Entity 
            BoardEntity boardEntity = BoardEntity.toBoardEntity(boardDto);
            boardRepository.save(boardEntity);

        } else {
            // there has some File..
            // Bring FILE DTO
            File directory = new File(FILE_PATH);
            if(!directory.exists()){
                if(!directory.mkdirs()){
                    throw new IOException("파일 저장 경로를 생성할 수 없습니다: " + FILE_PATH);
                }
            }

           MultipartFile boardFile = boardDto.getBoardFile();
           String originalFileName = boardFile.getOriginalFilename();

           if (originalFileName == null || originalFileName.isEmpty()) {
                // 파일은 있으나 파일명이 없는 예외적인 경우 처리 (혹은 오류 던지기)
                throw new IllegalArgumentException("업로드된 파일의 원본 파일명이 유효하지 않습니다.");
            }

           UUID uuid = UUID.randomUUID();
           String newFileName = uuid + "_" + originalFileName;
           String filePath = FILE_PATH + newFileName;

           // Acutally FileSave..
           boardFile.transferTo(new File(filePath)); // saveFile to Path ...
           boardDto.setAttachFile(1);

           // Board Save After -> FileSave 
           BoardEntity boardEntity = BoardEntity.toBoardEntity(boardDto);
           // SAVE 
           boardEntity = boardRepository.save(boardEntity);
           // FileSave
           BoardImgEntity boardImgEntity = BoardImgEntity.toInsertFile(BoardImgDto.builder()
                                        .oldName(originalFileName)
                                        .newName(newFileName)
                                        .boardEntity(boardEntity)                                 
                                        .build());
            boardImgRepository.save(boardImgEntity); // SAVE FILE 
       
        }
    }
    
    @Override
    public Page<BoardDto> boardListPage(Pageable pageable, String subject, String search) {
       Page<BoardEntity> boardEntities = null;

       if( subject==null || search==null || search.equals("")){
        boardEntities = boardRepository.findAll(pageable);
       } else {
            if( subject.equals("title")){
                boardEntities = boardRepository.findByTitleContaining(pageable, search);
            } else if( subject.equals("content")){
                boardEntities = boardRepository.findByContentContaining(pageable, search);
            } else {
                boardEntities = boardRepository.findAll(pageable);
            }
       }
       return boardEntities.map(BoardDto::toBoardDto);
    }

    @Override
    public BoardDto boardDetail(Long boardId) {
       BoardEntity boardEntity = boardRepository.findById(boardId)
                .orElseThrow(()-> new IllegalArgumentException("게시글 아이디가 존재하지 않음" + boardId));
        return BoardDto.toBoardDto(boardEntity);        
    }

    @Transactional
    @Override
    public void update(BoardDto boardDto) throws IOException {
        // 기존게시물이 있니?
        BoardEntity boardEntity = boardRepository.findById(boardDto.getId())
            .orElseThrow(()-> new IllegalArgumentException("수정할 게시물이 없습니다"));
        
        // DTO Value -> Entity Update 
        boardEntity.setTitle(boardDto.getTitle());
        boardEntity.setContent(boardDto.getContent());

        // there is NewFile?
        // File fix Logic 
        if(boardDto.getBoardFile() != null && !boardDto.getBoardFile().isEmpty()) {
             File directory = new File(FILE_PATH);
            if (!directory.exists()) {
                if (!directory.mkdirs()) {
                    throw new IOException("파일 저장 경로를 생성할 수 없습니다: " + FILE_PATH);
                }
            }
            // there has NewFile 
            MultipartFile newFile = boardDto.getBoardFile();

            // Original File Check First.... then Delete 
            Optional<BoardImgEntity> existImgOptional = boardImgRepository.findByBoardEntity(boardEntity);
            // there has OldFile..
            if(existImgOptional.isPresent()){
                BoardImgEntity existFileEntity = existImgOptional.get();
                // Psycially Image Delete ->
                File oldFile = new File(FILE_PATH + existFileEntity.getNewName());
                if(oldFile.exists()){
                    oldFile.delete();
                }
                // Also you Delete DB Data info(img)
                boardImgRepository.delete(existFileEntity);
            }
            // and Replace NewFile 
            String originalFileName = newFile.getOriginalFilename();
            UUID uuid = UUID.randomUUID();
            String newFileName = uuid + "_" + originalFileName;
            String filePath = FILE_PATH + newFileName;
            newFile.transferTo(new File(filePath));
            // And Change AttachFile Statue...
            boardEntity.setAttachFile(1);
            // Also SAVE New IMG Data into DB...-> 
            BoardImgEntity newFileEntity = BoardImgEntity.toInsertFile(BoardImgDto.builder()
                                                        .oldName(originalFileName)
                                                        .newName(newFileName)
                                                        .boardEntity(boardEntity)                                                    
                                                        .build());
            boardImgRepository.save(newFileEntity);    
        } else {
            // if there has No NEWFILE... -> 
        }
        // Save Entity 
        // Alredy Have id, JPA run UPDATE Query.
        boardRepository.save(boardEntity);

    }


    @Override
    public void deleteBoard(Long boardId) {

        BoardEntity boardEntity = boardRepository.findById(boardId)
                    .orElseThrow(()-> new IllegalArgumentException("게시글이 존재하지 않음"));
                    

        Optional<BoardImgEntity> fileOptional = boardImgRepository.findByBoardEntity(boardEntity);
        if(fileOptional.isPresent()){
            BoardImgEntity boardImgEntity = fileOptional.get();

            File realDelete = new File(FILE_PATH + boardImgEntity.getNewName());
            if(realDelete.exists()){
                realDelete.delete();
            }
            // DB delete
            boardImgRepository.delete(boardImgEntity);
        }
        // Board Entity Delete 
        boardRepository.delete(boardEntity);

    }

    // 조회수
    @Override
    @Transactional
    public void upHitDo(Long id) {
      boardRepository.updateHit(id);
    }

   
    
}
