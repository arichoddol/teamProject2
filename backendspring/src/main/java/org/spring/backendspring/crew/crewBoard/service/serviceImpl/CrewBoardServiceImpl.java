package org.spring.backendspring.crew.crewBoard.service.serviceImpl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.spring.backendspring.crew.crew.repository.CrewRepository;
import org.spring.backendspring.member.entity.MemberEntity;
import org.spring.backendspring.member.repository.MemberRepository;
import org.spring.backendspring.common.dto.PagedResponse;
import org.spring.backendspring.crew.crew.entity.CrewEntity;
import org.spring.backendspring.crew.crewBoard.dto.CrewBoardDto;
import org.spring.backendspring.crew.crewBoard.entity.CrewBoardEntity;
import org.spring.backendspring.crew.crewBoard.entity.CrewBoardImageEntity;
import org.spring.backendspring.crew.crewBoard.repository.CrewBoardImageRepository;
import org.spring.backendspring.crew.crewBoard.repository.CrewBoardRepository;
import org.spring.backendspring.crew.crewBoard.service.CrewBoardService;
import org.spring.backendspring.s3.AwsS3Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CrewBoardServiceImpl implements CrewBoardService {

    private final CrewRepository crewRepository;
    private final CrewBoardRepository crewBoardRepository;
    private final CrewBoardImageRepository crewBoardImageRepository;
    private final MemberRepository memberRepository;
    private final AwsS3Service awsS3Service;

    @Override
    public PagedResponse<CrewBoardDto> boardListByCrew(Long crewId, String subject, String keyword, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);

        Page<CrewBoardEntity> crewBoardPage;

        if (subject == null || keyword == null || keyword.trim().isEmpty()) {
            crewBoardPage = crewBoardRepository.findByCrewEntity_Id(crewId, pageRequest);
        } else {
            if (subject.equals("제목")) {
                crewBoardPage = crewBoardRepository.findByCrewEntity_IdAndTitleContaining(crewId, keyword, pageRequest);
            } else if (subject.equals("내용")) {
                crewBoardPage = crewBoardRepository.findByCrewEntity_IdAndContentContaining(crewId, keyword, pageRequest);
            } else if (subject.equals("작성자")) {
                crewBoardPage = crewBoardRepository.findByCrewEntity_IdAndMemberEntity_NickNameContaining(crewId, keyword, pageRequest);
            } else if (subject.equals("전체")) {
                crewBoardPage = crewBoardRepository.findByCrewEntity_IdAndTitleContainingOrContentContainingOrMemberEntity_NickNameContaining(crewId, keyword, keyword, keyword, pageRequest);
            } else {
                crewBoardPage = crewBoardRepository.findByCrewEntity_Id(crewId, pageRequest);
            }
        }

        Page<CrewBoardDto> mycrewBoardPage = crewBoardPage.map(CrewBoardDto::toDto2);

        return PagedResponse.of(mycrewBoardPage);
    }

    @Override
    public CrewBoardDto createBoard(Long crewId, CrewBoardDto crewBoardDto, Long loginUserId) throws IOException {
        CrewEntity crewEntity = crewRepository.findById(crewId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 크루"));

        MemberEntity memberEntity = memberRepository.findById(loginUserId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원"));

        crewRepository.findByIdAndCrewMemberEntities_MemberEntity_Id(crewId, loginUserId)
                .orElseThrow(() -> new IllegalArgumentException("크루 회원이 아닙니다."));

        crewBoardDto.setMemberId(memberEntity.getId());
        crewBoardDto.setCrewId(crewId);

        CrewBoardEntity crewBoardEntity = CrewBoardEntity.toCrewBoardEntity(crewBoardDto);
        
        CrewBoardEntity savedBoard = null;

        List<MultipartFile> crewBoardFile = crewBoardDto.getCrewBoardFile();
        
        if (crewBoardFile == null || crewBoardFile.isEmpty() || crewBoardFile.get(0).isEmpty()) {
            savedBoard = crewBoardRepository.save(crewBoardEntity);            
        } else {
            List<CrewBoardImageEntity> savedImages = new ArrayList<>();
            // for (MultipartFile file : crewBoardDto.getCrewBoardFile()) {
                //     if (file != null && !file.isEmpty()) {
                    //         String originalFileName = file.getOriginalFilename();
                    
                    //         String newFileName = awsS3Service.uploadFile(file);
                    
                    //         CrewBoardImageEntity boardImageEntity = CrewBoardImageEntity.toCrewBoardImageEntity(crewBoardEntity2, originalFileName, newFileName);
                    
                    //         crewBoardImageRepository.save(boardImageEntity);
                    //         crewBoardRepository.save(crewBoardEntity);
                    //     }
            // }
            for (MultipartFile boardFile : crewBoardFile) {
                if (boardFile != null && !boardFile.isEmpty()) {
                    UUID uuid = UUID.randomUUID();
                    String originalFileName = boardFile.getOriginalFilename();
                    String newFileName = uuid + "_" + originalFileName;

                    String filePath = "E:/full/upload/" + newFileName;
                    File file = new File(filePath);

                    boardFile.transferTo(file);
                    
                    CrewBoardImageEntity boardImageEntity = CrewBoardImageEntity.toCrewBoardImageEntity(crewBoardEntity, originalFileName, newFileName);
                    CrewBoardImageEntity savedImage = crewBoardImageRepository.save(boardImageEntity);
                    savedImages.add(savedImage);
                }
            }
            crewBoardEntity.setCrewBoardImageEntities(savedImages);
            savedBoard = crewBoardRepository.save(crewBoardEntity);            

        }
        return CrewBoardDto.toDto(savedBoard);
    }

    @Override
    public CrewBoardDto boardDetail(Long crewId, Long id) {

        CrewBoardEntity crewBoardEntity = crewBoardRepository.findByCrewEntity_IdAndId(crewId, id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글"));

        return CrewBoardDto.toDto(crewBoardEntity);
    }

    @Override
    public CrewBoardDto updateBoard(Long id, Long crewId, CrewBoardDto crewBoardDto,
                                    Long loginUserId,
                                    List<MultipartFile> newImages,
                                    List<String> deleteImageName) throws IOException {

        MemberEntity memberEntity = memberRepository.findById(loginUserId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원"));

        crewRepository.findByIdAndCrewMemberEntities_MemberEntity_Id(crewId, loginUserId)
                .orElseThrow(() -> new IllegalArgumentException("크루 회원이 아닙니다."));

        crewBoardDto.setMemberId(memberEntity.getId());
        crewBoardDto.setCrewId(crewId);

        CrewBoardEntity crewBoardEntity = crewBoardRepository.findByCrewEntity_IdAndId(crewId, id)
                .orElseThrow(() -> new NullPointerException("존재하지 않는 게시글"));
        
        // 제목, 내용 수정
        crewBoardEntity.setTitle(crewBoardDto.getTitle());
        crewBoardEntity.setContent(crewBoardDto.getContent());

        // 수정 후 이미지
        List<CrewBoardImageEntity> updatedImages = new ArrayList<>();

        // 기존 이미지
        List<CrewBoardImageEntity> currentImages = crewBoardImageRepository.findByCrewBoardEntity(crewBoardEntity);

        // 삭제할 이미지 삭제
        if (deleteImageName != null && !deleteImageName.isEmpty()) {
            for (String imageName : deleteImageName) {
                CrewBoardImageEntity imageEntity = crewBoardImageRepository.findByCrewBoardEntity_IdAndNewName(id, imageName)
                            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이미지"));
                            
                // awsS3Service.deleteFile(imageEntity.getNewName());
                crewBoardImageRepository.delete(imageEntity);
            }
        }

        // 삭제 후 조회
        updatedImages = crewBoardImageRepository.findByCrewBoardEntity(crewBoardEntity);

        // 새로운 이미지 추가
        if (newImages != null && !newImages.isEmpty()) {
            for (MultipartFile image : newImages) {
                if (!image.isEmpty()) {
                    // String originalImageName = image.getOriginalFilename();
                    // String newImageName = awsS3Service.uploadFile(image);
                    
                    // CrewBoardImageEntity imageEntity = CrewBoardImageEntity.toCrewBoardImageEntity(crewBoardEntity, originalImageName, newImageName);
                    // crewBoardImageRepository.save(imageEntity);

                    UUID uuid = UUID.randomUUID();
                    String originalFileName = image.getOriginalFilename();
                    String newFileName = uuid + "_" + originalFileName;

                    String filePath = "E:/full/upload/" + newFileName;
                    File file = new File(filePath);

                    image.transferTo(file);

                    CrewBoardImageEntity boardImageEntity = CrewBoardImageEntity.toCrewBoardImageEntity(crewBoardEntity, originalFileName, newFileName);
                    CrewBoardImageEntity savedImage = crewBoardImageRepository.save(boardImageEntity);
                    updatedImages.add(savedImage);
                }
            }
        }
        crewBoardEntity.setCrewBoardImageEntities(updatedImages);
        CrewBoardEntity updatedBoardEntity = crewBoardRepository.save(crewBoardEntity);

        return CrewBoardDto.toDto(updatedBoardEntity);
    }

    @Override
    public void deleteBoard(Long id, Long crewId, Long loginUserId) {
        CrewBoardEntity crewBoardEntity = crewBoardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글 삭제 불가"));

        crewRepository.findByIdAndCrewMemberEntities_MemberEntity_Id(crewId, loginUserId)
                .orElseThrow(() -> new IllegalArgumentException("크루 회원이 아닙니다."));
  
        MemberEntity memberEntity = memberRepository.findById(loginUserId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원"));

        List<CrewBoardImageEntity> crewBoardImages = crewBoardImageRepository.findByCrewBoardEntity_Id(id);
        if (crewBoardImages != null) {
            for (CrewBoardImageEntity images : crewBoardImages) {
                awsS3Service.deleteFile(images.getNewName());
            }
        }

        crewBoardImageRepository.deleteAll(crewBoardImages);
        crewBoardRepository.delete(crewBoardEntity);
    }



}
