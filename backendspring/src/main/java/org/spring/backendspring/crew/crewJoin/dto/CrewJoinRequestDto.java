package org.spring.backendspring.crew.crewJoin.dto;

import lombok.*;
import org.spring.backendspring.common.RequestStatus;
import org.spring.backendspring.crew.crew.entity.CrewEntity;
import org.spring.backendspring.crew.crewJoin.entity.CrewJoinRequestEntity;
import org.spring.backendspring.member.entity.MemberEntity;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class CrewJoinRequestDto {
    private Long id;
    private Long crewRequestId;
    private Long memberRequestId;
    private CrewEntity crewEntity;
    private MemberEntity memberEntity;
    private String message;
    private RequestStatus status;

    public static  CrewJoinRequestDto crewJoinRequestDto (CrewJoinRequestEntity crewJoinRequestEntity){
        return CrewJoinRequestDto.builder()
                .crewRequestId(crewJoinRequestEntity.getCrewEntity().getId())
                .memberRequestId(crewJoinRequestEntity.getMemberEntity().getId())
//                .crewEntity(crewJoinRequestEntity.getCrewEntity())
//                .memberEntity(crewJoinRequestEntity.getMemberEntity())
                .message(crewJoinRequestEntity.getMessage())
                .build();
    }
}
