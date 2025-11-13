package org.spring.backendspring.crew.crewBoard.service.serviceImpl;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.spring.backendspring.member.entity.MemberEntity;
import org.spring.backendspring.member.repository.MemberRepository;
import org.spring.backendspring.crew.crewBoard.dto.CrewBoardDto;
import org.spring.backendspring.crew.crewBoard.entity.CrewBoardEntity;
import org.spring.backendspring.crew.crewBoard.entity.CrewBoardImageEntity;
import org.spring.backendspring.crew.crewBoard.repository.CrewBoardImageRepository;
import org.spring.backendspring.crew.crewBoard.repository.CrewBoardRepository;
import org.spring.backendspring.crew.crewBoard.service.CrewBoardService;
import org.spring.backendspring.s3.AwsS3Service;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CrewBoardServiceImpl implements CrewBoardService {
    
    private final CrewBoardRepository crewBoardRepository;
    private final CrewBoardImageRepository crewBoardImageRepository;
    private final MemberRepository memberRepository;
    private final AwsS3Service awsS3Service;
    
    @Override
    public List<CrewBoardDto> boardListByCrew(Long crewId) {
    
        List<CrewBoardEntity> crewBoardEntityList = crewBoardRepository.findByCrewEntity_Id(crewId);
    
        if (crewBoardEntityList.isEmpty()) {
            throw new NullPointerException("조회할 게시글 없음");
        }
    
        return crewBoardEntityList.stream()
                    .map(CrewBoardDto::toDto2)
                    .collect(Collectors.toList());
    }

    @Override
    public CrewBoardDto createBoard(Long crewId, CrewBoardDto crewBoardDto) throws IOException {
        
        MemberEntity memberEntity = memberRepository.findById(crewBoardDto.getMemberId())
                .orElseThrow(IllegalArgumentException::new);

        crewBoardDto.setMemberEntity(memberEntity);
        crewBoardDto.setCrewId(crewId);

        List<MultipartFile> crewBoardFile = crewBoardDto.getCrewBoardFile();

        CrewBoardEntity crewBoardEntity;

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
                if (!file.isEmpty()) {
                    String originalFileName = file.getOriginalFilename();
                    
                    String newFileName = awsS3Service.uploadFile(file);

                    CrewBoardImageEntity boardImageEntity = CrewBoardImageEntity.toCrewBoardImageEntity(crewBoardEntity2, originalFileName, newFileName);

                    crewBoardImageRepository.save(boardImageEntity);
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
    public CrewBoardDto updateBoard(Long id, Long crewId, CrewBoardDto crewBoardDto) throws IOException {
        
        MemberEntity memberEntity = memberRepository.findById(crewBoardDto.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원"));

        crewBoardDto.setMemberEntity(memberEntity);
        crewBoardDto.setCrewId(crewId);

        CrewBoardEntity crewBoardEntity = crewBoardRepository.findByCrewEntity_IdAndId(crewId, id)
                .orElseThrow(() -> new NullPointerException("존재하지 않는 게시글"));
        
        List<CrewBoardImageEntity> crewBoardImageEntities = crewBoardImageRepository.findByCrewBoardEntity(crewBoardEntity);

        CrewBoardEntity updatedBoardEntity;
        if (crewBoardDto.getCrewBoardFile() == null || crewBoardDto.getCrewBoardFile().isEmpty()) {
            updatedBoardEntity = crewBoardRepository.save(CrewBoardEntity.toCrewBoardEntity(crewBoardDto));
        } else {
            for (CrewBoardImageEntity images : crewBoardImageEntities) {
                awsS3Service.deleteFile(images.getNewName());
                crewBoardImageRepository.delete(images);
            }

            List<MultipartFile> crewBoardFiles = crewBoardDto.getCrewBoardFile();

            updatedBoardEntity = crewBoardRepository.save(CrewBoardEntity.toCrewBoardEntity(crewBoardDto));

            if (crewBoardFiles != null) {
                for (MultipartFile images : crewBoardFiles) {
                    String originalImageName = images.getOriginalFilename();
                    String newImageName = awsS3Service.uploadFile(images);
                    
                    CrewBoardImageEntity boardImageEntity = CrewBoardImageEntity.toCrewBoardImageEntity(updatedBoardEntity, originalImageName, newImageName);
                    crewBoardImageRepository.save(boardImageEntity);
                }
            }
        }
        return CrewBoardDto.toDto(updatedBoardEntity, memberEntity);
    }

    @Override
    public void deleteBoard(Long id) {
        CrewBoardEntity crewBoardEntity = crewBoardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글 삭제 불가"));
        
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
