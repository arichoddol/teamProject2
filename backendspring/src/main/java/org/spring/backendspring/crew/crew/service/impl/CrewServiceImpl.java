package org.spring.backendspring.crew.crew.service.impl;

import lombok.RequiredArgsConstructor;
import org.spring.backendspring.crew.crew.dto.CrewDto;
import org.spring.backendspring.crew.crew.entity.CrewEntity;
import org.spring.backendspring.crew.crew.entity.CrewImageEntity;
import org.spring.backendspring.crew.crew.repository.CrewImageRepository;
import org.spring.backendspring.crew.crew.repository.CrewRepository;
import org.spring.backendspring.crew.crew.service.CrewService;
import org.spring.backendspring.s3.AwsS3Service;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CrewServiceImpl implements CrewService {

    private final CrewRepository crewRepository;
    private final CrewImageRepository crewImageRepository;
    private final AwsS3Service awsS3Service;

    @Override
    public CrewDto updateCrew(Long crewId, CrewDto crewDto,
                              List<MultipartFile> newImages,
                              List<Long> deleteImageId) throws IOException {

        CrewEntity crew = crewRepository.findById(crewId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 크루"));

        // 크루 기본 정보 수정
        crew.setName(crewDto.getName());

        // 삭제할 이미지
        if (deleteImageId != null && !deleteImageId.isEmpty()) {
            for (Long imageId : deleteImageId) {
                CrewImageEntity imageEntity = crewImageRepository.findById(imageId)
                        .orElseThrow(() -> new IllegalArgumentException("이미지가 존재하지 않음"));
                awsS3Service.deleteFile(imageEntity.getNewName());
                crewImageRepository.delete(imageEntity);
            }
        }

        // 새로운 이미지
        if (newImages != null && !newImages.isEmpty()) {
            for (MultipartFile image : newImages) {
                if (!image.isEmpty()) {
                    String originalImageName = image.getOriginalFilename();
                    String newImageName = awsS3Service.uploadFile(image);

                    CrewImageEntity imageEntity = CrewImageEntity.toEntity(crew, originalImageName, newImageName);
                    crewImageRepository.save(imageEntity);
                }
            }
        }

        CrewEntity updated = crewRepository.save(crew);
        return CrewDto.toCrewDto(updated);
    }

    @Override
    public void deleteCrew(Long crewId) {
        CrewEntity crewEntity = crewRepository.findById(crewId)
                .orElseThrow(IllegalArgumentException::new);

        List<CrewImageEntity> crewImages = crewImageRepository.findByCrewEntity_Id(crewId);
        if (crewImages != null) {
            for (CrewImageEntity img : crewImages) {
                awsS3Service.deleteFile(img.getNewName());
            }
        }

        crewImageRepository.deleteAll(crewImages);
        crewRepository.delete(crewEntity);
    }

    @Override
    public List<CrewDto> crewList() {
        List<CrewEntity> crewEntities = crewRepository.findAll();

        if (crewEntities.isEmpty()) {
            throw new NullPointerException("조회할 목록 없음");
        }

        return crewEntities.stream()
                .map(CrewDto::toCrewDto)
                .collect(Collectors.toList());
    }

    @Override
    public CrewDto crewDetail(Long crewId) {
        CrewEntity crewEntity = crewRepository.findById(crewId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 크루"));

        return CrewDto.toCrewDto(crewEntity);
    }

    // @Override
    // public CrewDto findCrew(Long crewId) {
    //     CrewEntity crewEntity = crewRepository.findById(crewId)
    //                  .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 크루"));
    //     return CrewDto.toCrewDto(crewEntity);
    // }

    @Override
    public List<CrewDto> findAllCrew() {
        return crewRepository.findAll().stream().map(CrewDto::toCrewDto).collect(Collectors.toList());
    }

    @Override
    public CrewDto findMyCrew(Long crewId, Long memberId) {
        CrewEntity crewEntity = crewRepository.findById(crewId)
                .orElseThrow(() -> new IllegalArgumentException("크루를 찾을 수 없습니다."));

        crewEntity.getCrewMemberEntities().stream()
                .filter(member -> member.getMemberId().equals(memberId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("이 크루의 멤버가 아닙니다."));

        return CrewDto.toCrewDto(crewEntity);
    }
}
