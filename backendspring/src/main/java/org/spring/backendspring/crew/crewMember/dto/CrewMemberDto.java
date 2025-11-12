package org.spring.backendspring.crew.crewMember.dto;

import lombok.*;
import org.spring.backendspring.common.BasicTime;
import org.spring.backendspring.common.role.CrewRole;
import org.spring.backendspring.crew.crew.entity.CrewEntity;
import org.spring.backendspring.crew.crewMember.entity.CrewMemberEntity;
import org.spring.backendspring.member.entity.MemberEntity;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CrewMemberDto extends BasicTime {
    private Long id;

    private CrewEntity crewEntity;

    private MemberEntity memberEntity;

    private CrewRole roleInCrew; // LEADER/MEMBER

    public static CrewMemberDto toCrewMember (CrewMemberEntity crewMemberEntity){

        return CrewMemberDto.builder()
                .id(crewMemberEntity.getId())
                .crewEntity(crewMemberEntity.getCrewEntity())
                .memberEntity(crewMemberEntity.getMemberEntity())
                .roleInCrew(crewMemberEntity.getRoleInCrew())
                .build();
    }
}