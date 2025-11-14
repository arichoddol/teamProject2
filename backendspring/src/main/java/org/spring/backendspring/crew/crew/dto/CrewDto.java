package org.spring.backendspring.crew.crew.dto;

import lombok.*;
import org.spring.backendspring.common.BasicTime;
import org.spring.backendspring.crew.crewBoard.entity.CrewBoardEntity;
import org.spring.backendspring.crew.crewJoin.entity.CrewJoinRequestEntity;
import org.spring.backendspring.crew.crewMember.entity.CrewMemberEntity;
import org.spring.backendspring.crew.crew.entity.CrewEntity;
import org.spring.backendspring.crew.crew.entity.CrewImageEntity;
import org.spring.backendspring.crew.crewRun.entity.CrewRunEntity;
import org.spring.backendspring.member.entity.MemberEntity;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
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

    private Long memberId;

    public static CrewDto toCrewDto(CrewEntity crewEntity) {
        return CrewDto.builder()
                .id(crewEntity.getId())
                .name(crewEntity.getName())
                .description(crewEntity.getDescription())
                .district(crewEntity.getDistrict())
                .isCrewImg(crewEntity.getIsCrewImg())
                .memberId(crewEntity.getMemberEntity().getId())
                .crewImageEntities(crewEntity.getCrewImageEntities())
                .crewMemberEntities(crewEntity.getCrewMemberEntities())
//                .crewJoinRequestEntities(crewEntity.getCrewJoinRequestEntities())
                .createTime(crewEntity.getCreateTime())
                .upDateTime(crewEntity.getUpdateTime())
                .build();
    }

    public static CrewDto toCrewDtoImg(CrewEntity crewEntity) {
        return CrewDto.builder()
                .id(crewEntity.getId())
                .name(crewEntity.getName())
                .description(crewEntity.getDescription())
                .district(crewEntity.getDistrict())
                .isCrewImg(1)
                .memberId(crewEntity.getMemberEntity().getId())
                .crewImageEntities(crewEntity.getCrewImageEntities())
                // .crewMemberEntities(crewEntity.getCrewMemberEntities())
//                .crewJoinRequestEntities(crewEntity.getCrewJoinRequestEntities())
                .createTime(crewEntity.getCreateTime())
                .upDateTime(crewEntity.getUpdateTime())
                .build();
    }
}