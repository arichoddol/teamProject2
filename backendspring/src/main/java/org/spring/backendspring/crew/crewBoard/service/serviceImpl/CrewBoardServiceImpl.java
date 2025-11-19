package org.spring.backendspring.crew.crewBoard.service.serviceImpl;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public PagedResponse<CrewBoardDto> boardListByCrew(Long crewId, String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        Page<CrewBoardDto> crewBoardPage;

        if (keyword == null || keyword.trim().isEmpty()) {
            crewBoardPage = crewBoardRepository.findByCrewEntity_Id(crewId, pageable)
                        .map(CrewBoardDto::toDto2);
        } else {
            crewBoardPage = crewBoardRepository
                    .findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(keyword, keyword, pageable)
                    .map(CrewBoardDto::toDto2);
        }

        return PagedResponse.of(crewBoardPage);
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

        List<MultipartFile> crewBoardFile = crewBoardDto.getCrewBoardFile();

        CrewBoardEntity crewBoardEntity = CrewBoardEntity.toCrewBoardEntity(crewBoardDto);

        if (crewBoardFile == null || crewBoardFile.isEmpty() || crewBoardFile.get(0).isEmpty()) {
            crewBoardEntity = CrewBoardEntity.toCrewBoardEntity(crewBoardDto);
            crewBoardRepository.save(crewBoardEntity);
        } else {
            crewBoardEntity = CrewBoardEntity.toCrewBoardEntity(crewBoardDto);
            Long boardId = crewBoardRepository.save(crewBoardEntity).getId();
            CrewBoardEntity crewBoardEntity2 = crewBoardRepository.findById(boardId)
            .orElseThrow(() -> {
                throw new IllegalArgumentException("존재하지 않는 게시글");
            });
            
            for (MultipartFile file : crewBoardDto.getCrewBoardFile()) {
                if (file != null && !file.isEmpty()) {
                    String originalFileName = file.getOriginalFilename();

                    String newFileName = awsS3Service.uploadFile(file);
                    
                    CrewBoardImageEntity boardImageEntity = CrewBoardImageEntity.toCrewBoardImageEntity(crewBoardEntity2, originalFileName, newFileName);
                    
                    crewBoardImageRepository.save(boardImageEntity);
                    crewBoardRepository.save(crewBoardEntity);
                }
            }

        }
        return CrewBoardDto.toDto(crewBoardEntity, memberEntity);
    }

    @Override
    public CrewBoardDto boardDetail(Long crewId, Long id) {

        CrewBoardEntity crewBoardEntity = crewBoardRepository.findByCrewEntity_IdAndId(crewId, id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글"));

        return CrewBoardDto.toDto2(crewBoardEntity);
    }

    @Override
    public CrewBoardDto updateBoard(Long id, Long crewId, CrewBoardDto crewBoardDto, Long loginUserId,
                                    List<MultipartFile> newImages,
                                    List<Long> deleteImageId) throws IOException {

        MemberEntity memberEntity = memberRepository.findById(loginUserId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원"));

        crewRepository.findByIdAndCrewMemberEntities_MemberEntity_Id(crewId, loginUserId)
                .orElseThrow(() -> new IllegalArgumentException("크루 회원이 아닙니다."));

        crewBoardDto.setMemberId(memberEntity.getId());
        crewBoardDto.setCrewId(crewId);

        CrewBoardEntity crewBoardEntity = crewBoardRepository.findByCrewEntity_IdAndId(crewId, id)
                .orElseThrow(() -> new NullPointerException("존재하지 않는 게시글"));
                    
        crewBoardEntity.setTitle(crewBoardDto.getTitle());
        crewBoardEntity.setContent(crewBoardDto.getContent());

        // 삭제할 이미지
        if (deleteImageId != null && !deleteImageId.isEmpty()) {
            for (Long imageId : deleteImageId) {
                CrewBoardImageEntity imageEntity = crewBoardImageRepository.findById(imageId)
                            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이미지"));
                awsS3Service.deleteFile(imageEntity.getNewName());
                crewBoardImageRepository.delete(imageEntity);
            }
        }

        // 새로운 이미지
        if (newImages != null && !newImages.isEmpty()) {
            for (MultipartFile image : newImages) {
                if (!image.isEmpty()) {
                    String originalImageName = image.getOriginalFilename();
                    String newImageNaem = awsS3Service.uploadFile(image);

                    CrewBoardImageEntity imageEntity = CrewBoardImageEntity.toCrewBoardImageEntity(crewBoardEntity, originalImageName, newImageNaem);
                    crewBoardImageRepository.save(imageEntity);
                }
            }
        }

        CrewBoardEntity updatedBoardEntity = crewBoardRepository.save(crewBoardEntity);

        return CrewBoardDto.toDto(updatedBoardEntity, memberEntity);
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
