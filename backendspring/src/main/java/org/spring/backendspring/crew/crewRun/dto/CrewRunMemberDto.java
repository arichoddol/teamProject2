package org.spring.backendspring.crew.crewRun.dto;

import lombok.*;
import org.spring.backendspring.common.AttendeeStatus;
import org.spring.backendspring.common.BasicTime;
import org.spring.backendspring.crew.crewRun.entity.CrewRunEntity;
import org.spring.backendspring.crew.crewRun.entity.CrewRunMemberEntity;
import org.spring.backendspring.member.entity.MemberEntity;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class CrewRunMemberDto extends BasicTime {
    private Long id;


    private CrewRunEntity crewRunEntity; // 어떤 일정에 참석?


    private MemberEntity memberEntity; // 누가 참석?

    private String memberNickName;
    private Long memberId;

    private AttendeeStatus status; // YES / NO

    public static CrewRunMemberDto toCrewRunMemberDto(CrewRunMemberEntity crewRunMemberEntity){
        return CrewRunMemberDto.builder()
                .id(crewRunMemberEntity.getId())
                .crewRunEntity(crewRunMemberEntity.getCrewRunEntity())
                .memberNickName(crewRunMemberEntity.getMemberEntity().getNickName())
                .build();
    }
}