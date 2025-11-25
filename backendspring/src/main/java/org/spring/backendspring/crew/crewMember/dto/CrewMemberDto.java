package org.spring.backendspring.crew.crewMember.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.*;
import org.spring.backendspring.common.BasicTime;
import org.spring.backendspring.common.role.CrewRole;
import org.spring.backendspring.crew.crew.entity.CrewImageEntity;
import org.spring.backendspring.crew.crewMember.entity.CrewMemberEntity;
import org.spring.backendspring.member.entity.MemberEntity;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CrewMemberDto extends BasicTime {

    private Long id;

    private Long crewId;
    private Long memberId;

//    private CrewEntity crewEntity;
    private String crewName;
    private String description;
    private String district;
    private List<CrewImageEntity> crewImageEntities;

    private MemberEntity memberEntity;

    private CrewRole roleInCrew; // LEADER/MEMBER

    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    // mycrew 목록 위한 이미지
    private List<String> crewImages;

    private int crewMembers;

    public static CrewMemberDto toCrewMember(CrewMemberEntity crewMemberEntity) {
        List<String> crewImages = crewMemberEntity.getCrewEntity().getCrewImageEntities()
                    .stream().map(CrewImageEntity::getNewName)
                    .toList();
        int members = crewMemberEntity.getCrewEntity().getCrewMemberEntities().size();
        //보이고 싶은 정보 추가
        return CrewMemberDto.builder()
                .id(crewMemberEntity.getId())
                .crewId(crewMemberEntity.getCrewEntity().getId())
                .memberId(crewMemberEntity.getMemberEntity().getId())
                .crewName(crewMemberEntity.getCrewEntity().getName())
                .description(crewMemberEntity.getCrewEntity().getDescription())
                .district(crewMemberEntity.getCrewEntity().getDistrict())
                // .crewImageEntities(crewMemberEntity.getCrewEntity().getCrewImageEntities())
//                .memberEntity(crewMemberEntity.getMemberEntity())
                .roleInCrew(crewMemberEntity.getRoleInCrew())
                .createTime(crewMemberEntity.getCreateTime())
                .updateTime(crewMemberEntity.getUpdateTime())
                .crewImages(crewImages)
                .crewMembers(members)
                .build();
    }
}