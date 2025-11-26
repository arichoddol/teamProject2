package org.spring.backendspring.crew.crew.dto;

import lombok.*;
import org.spring.backendspring.common.BasicTime;
import org.spring.backendspring.config.s3.AwsS3Service;
import org.spring.backendspring.crew.crewBoard.entity.CrewBoardEntity;
import org.spring.backendspring.crew.crewBoard.entity.CrewBoardImageEntity;
import org.spring.backendspring.crew.crewJoin.entity.CrewJoinRequestEntity;
import org.spring.backendspring.crew.crewMember.entity.CrewMemberEntity;
import org.spring.backendspring.crew.crew.entity.CrewEntity;
import org.spring.backendspring.crew.crew.entity.CrewImageEntity;
import org.spring.backendspring.crew.crewRun.entity.CrewRunEntity;
import org.spring.backendspring.member.entity.MemberEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CrewDto extends BasicTime {

    private Long id;

    private String name; // 크루명

    private String description;

    private String district;

    private int isCrewImg; // 0/1 대표 이미지

    private MemberEntity memberEntity; // 개설자z

    private List<CrewImageEntity> crewImageEntities;

    private List<CrewMemberEntity> crewMemberEntities;

    private List<CrewJoinRequestEntity> crewJoinRequestEntities;

    private List<CrewRunEntity> crewRunEntities;

    private List<CrewBoardEntity> crewBoardEntities;

    private LocalDateTime createTime;
    private LocalDateTime upDateTime;

    private List<MultipartFile> imageFile;
    private List<String> oldFileName;
    private List<String> newFileName;
    private List<String> fileUrl;

    private Long memberId;
    private String memberNickName;

    public static CrewDto toCrewDto(CrewEntity crewEntity) {
        List<String> newFileName = crewEntity.getCrewImageEntities().stream()
                    .map(CrewImageEntity::getNewName)
                    .toList();
        List<String> originalFileName = crewEntity.getCrewImageEntities().stream()
                    .map(CrewImageEntity::getOldName)
                    .toList();

        return CrewDto.builder()
                .id(crewEntity.getId())
                .name(crewEntity.getName())
                .description(crewEntity.getDescription())
                .district(crewEntity.getDistrict())
                .isCrewImg(crewEntity.getIsCrewImg())
                .memberId(crewEntity.getMemberEntity().getId())
                // .crewImageEntities(crewEntity.getCrewImageEntities())
                .crewMemberEntities(crewEntity.getCrewMemberEntities())
//                .crewJoinRequestEntities(crewEntity.getCrewJoinRequestEntities())
                .createTime(crewEntity.getCreateTime())
                .upDateTime(crewEntity.getUpdateTime())
                .oldFileName(originalFileName)
                .newFileName(newFileName)
                .memberNickName(crewEntity.getMemberEntity().getNickName())
                .build();
    }

    public static CrewDto toCrewDtoS3(CrewEntity crewEntity, AwsS3Service awsS3Service) {
        List<String> newFileName = crewEntity.getCrewImageEntities().stream()
                    .map(CrewImageEntity::getNewName)
                    .toList();
        List<String> originalFileName = crewEntity.getCrewImageEntities().stream()
                    .map(CrewImageEntity::getOldName)
                    .toList();
        List<String> updatedFileUrl = new ArrayList<>();
        for (CrewImageEntity img : crewEntity.getCrewImageEntities()) {
            try {
                String url = awsS3Service.getFileUrl(img.getNewName());
                updatedFileUrl.add(url);
            } catch (IOException e) {
                throw new IllegalArgumentException("S3 URL 생성 실패", e);
            }
        }

        return CrewDto.builder()
                .id(crewEntity.getId())
                .name(crewEntity.getName())
                .description(crewEntity.getDescription())
                .district(crewEntity.getDistrict())
                .isCrewImg(crewEntity.getIsCrewImg())
                .memberId(crewEntity.getMemberEntity().getId())
                // .crewImageEntities(crewEntity.getCrewImageEntities())
                .crewMemberEntities(crewEntity.getCrewMemberEntities())
//                .crewJoinRequestEntities(crewEntity.getCrewJoinRequestEntities())
                .createTime(crewEntity.getCreateTime())
                .upDateTime(crewEntity.getUpdateTime())
                .oldFileName(originalFileName)
                .newFileName(newFileName)
                .fileUrl(updatedFileUrl)
                .memberNickName(crewEntity.getMemberEntity().getNickName())
                .build();
    }
    public static CrewDto toCrewDto2(CrewEntity crewEntity, List<String> fileUrl) {
        List<String> newFileName = crewEntity.getCrewImageEntities().stream()
                    .map(CrewImageEntity::getNewName)
                    .toList();
        List<String> originalFileName = crewEntity.getCrewImageEntities().stream()
                    .map(CrewImageEntity::getOldName)
                    .toList();

        return CrewDto.builder()
                .id(crewEntity.getId())
                .name(crewEntity.getName())
                .description(crewEntity.getDescription())
                .district(crewEntity.getDistrict())
                .isCrewImg(crewEntity.getIsCrewImg())
                .memberId(crewEntity.getMemberEntity().getId())
                // .crewImageEntities(crewEntity.getCrewImageEntities())
                .crewMemberEntities(crewEntity.getCrewMemberEntities())
//                .crewJoinRequestEntities(crewEntity.getCrewJoinRequestEntities())
                .createTime(crewEntity.getCreateTime())
                .upDateTime(crewEntity.getUpdateTime())
                .oldFileName(originalFileName)
                .newFileName(newFileName)
                .fileUrl(fileUrl)
                .memberNickName(crewEntity.getMemberEntity().getNickName())
                .build();
    }
}