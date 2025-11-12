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

    // @Transactional
    // @Override
    // public void insertBoard(BoardDto boardDto) throws IOException {
    //     // MemberCheck 
    //     MemberEntity memberEntity = memberRepository.findById(boardDto.getMemberId())
    //                             .orElseThrow(IllegalArgumentException::new);
    //     boardDto.setMemberEntity(memberEntity);
    //     // if file is Empty
    //     if (boardDto.getBoardFile().isEmpty()){
    //         boardDto.setAttachFile(0);
    //         boardDto.setMemberEntity(memberEntity);
    //         // DTO -> Entity 
    //         BoardEntity boardEntity = BoardEntity.toBoardEntity(boardDto);
    //         boardRepository.save(boardEntity);
    //     } else {
    //         // imple later AWS S3 bucket Link
    //         // there has file 
    //         MultipartFile boardFile =boardDto.getBoardFile();
    //         String originalFileName = boardFile.getOriginalFilename();
    //         UUID uuid = UUID.randomUUID();
    //         newFileName = uuid+"_"+originalFileName;
    //         // String filePath = " AWS S3?"
    //     }
    // }

    @Override
    @Transactional
    public void insertBoard(BoardDto boardDto) throws IOException {
        
        // Membmer Check 
        MemberEntity memberEntity = memberRepository.findById(boardDto.getMemberId())
                    .orElseThrow(IllegalArgumentException::new);
        boardDto.setMemberentity(memberEntity);


        // List<MultipartFile> boMultipartFiles = boardDto.getBoardFileList();
        if(boardDto.getBoardFile().isEmpty()){
           
            boardDto.setAttachFile(0);
            boardDto.setMemberentity(memberEntity);
         
             // DTO -> Entity 
            BoardEntity boardEntity = BoardEntity.toBoardEntity(boardDto);
            boardRepository.save(boardEntity);

        } else {
            // there has some File..
            // Bring FILE DTO

            // DTO -> Entity
            BoardEntity boardEntity = BoardEntity.toBoardEntity(boardDto);
            // Entity -> DB -> Bring PrimaryKey ID
            Long boardId = boardRepository.save(boardEntity).getId();
            // Use ID for Bring Currently Board(entity)
            BoardEntity boardEntity1 = boardRepository.findById(boardId).orElseThrow(()->{
                throw new IllegalArgumentException("X");
            });
            // DTO에 담겨온 첨부 파일 목록을 하나씩 반복 처리
            for(MultipartFile multipartFile: boardDto.getBoardFileList()){

                if (!multipartFile.isEmpty()) {
                    String originalFileName = multipartFile.getOriginalFilename();
                    // S3 File 
                    String oldFileName2 = awsS3Service.uploadFile(multipartFile);

                    // 파일 정보 Entity 생성 (게시글 Entity1, 원본 이름, S3 저장 이름 사용)
                    BoardImgEntity boardImgEntity = BoardImgEntity.toBoardImgEntity(boardEntity1, originalFileName, oldFileName2);
                    // 생성된 파일 정보를 DB의 'board_img' 테이블에 저장
                    boardImgRepository.save(boardImgEntity);
                }

            }
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

        BoardEntity boardEntity = boardRepository.findById(boardDto.getId())
                .orElseThrow(()-> new IllegalArgumentException("수정할 게시글이 없습니다."));
        
        // DTO -> entity
        boardEntity.setTitle(boardDto.getTitle());
        boardEntity.setContent(boardDto.getContent());
        // newFile Check 
        if(boardDto.getBoardFile() != null && !boardDto.getBoardFile().isEmpty()){
            // has newFile
            List<MultipartFile> newFilelist = boardDto.getBoardFileList();
            MultipartFile newFile = boardDto.getBoardFile();

            // if there is OldFile... >> REMOVE
            Optional<BoardImgEntity> existingFileOptional = boardImgRepository.findByBoardEntity(boardEntity);
            // File :: 1
            if(existingFileOptional.isPresent()){
                BoardImgEntity existingImg = existingFileOptional.get();

                String oldFileKey = existingImg.getNewName();
                // awsS3Service.deleteObject("buketec2", oldFileKey);

                // DB data Delete 
                boardImgRepository.delete(existingImg);

            }
            // Add New File Save 
            String originalFileName = newFile.getOriginalFilename();
            String newFileName = UUID.randomUUID() + "_" + originalFileName; // this is S3 Key 
            
            // prepare S3 fileUpload
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(newFile.getContentType());
            metadata.setContentLength(newFile.getSize());

            // S3 FileUpload 
            // awsS3Service.putObject("bucketec2", newFileName, newFile.getInputStream(), metadata);

            // entitiy AttachFile statue
            boardEntity.setAttachFile(1);

            // new Fileinfo Save DB 
            BoardImgEntity newBoardImgEntity = BoardImgEntity.toInsertFile(BoardImgDto.builder()
                                        .oldName(originalFileName)
                                        .newName(newFileName)
                                        .boardEntity(boardEntity)
                                        .build());
            boardImgRepository.save(newBoardImgEntity);
        } else {
            //

        }
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
