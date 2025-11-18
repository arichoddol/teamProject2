package org.spring.backendspring.crew.crewBoard.dto;

import java.time.LocalDateTime;
import java.util.List;

import org.spring.backendspring.crew.crew.entity.CrewEntity;
import org.spring.backendspring.crew.crewBoard.entity.CrewBoardCommentEntity;
import org.spring.backendspring.crew.crewBoard.entity.CrewBoardEntity;
import org.spring.backendspring.crew.crewBoard.entity.CrewBoardImageEntity;
import org.spring.backendspring.member.entity.MemberEntity;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CrewBoardDto {

    private Long id;

    private String title; // 제목

    private String content; // 내용

    private CrewEntity crewEntity; // 소속 크루

//    private MemberEntity memberEntity; // 작성자

    private List<CrewBoardCommentEntity> crewBoardCommentEntities; // 댓글

    private List<CrewBoardImageEntity> crewBoardImageEntities ;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    private List<MultipartFile> crewBoardFile;
    private List<String> originalFileName;
    private List<String> newFileName;

    private Long crewId;
    private Long memberId;
    private String memberNickName;

    public static CrewBoardDto toDto(CrewBoardEntity entity, MemberEntity memberEntity) {

        return CrewBoardDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .crewId(entity.getCrewEntity().getId())
                .memberId(entity.getMemberEntity().getId())
//                .memberEntity(memberEntity)
                .memberNickName(entity.getMemberEntity().getNickName())
                .crewBoardCommentEntities(entity.getCrewBoardCommentEntities())
                .crewBoardImageEntities(entity.getCrewBoardImageEntities())
                .createTime(entity.getCreateTime())
                .updateTime(entity.getUpdateTime())
                .build();
    }
    public static CrewBoardDto toDto2(CrewBoardEntity entity) {

        return CrewBoardDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .crewId(entity.getCrewEntity().getId())
                .memberId(entity.getMemberEntity().getId())
                .memberNickName(entity.getMemberEntity().getNickName())
                // .crewBoardCommentEntities(entity.getCrewBoardCommentEntities())
                // .crewBoardImageEntities(entity.getCrewBoardImageEntities())
                .createTime(entity.getCreateTime())
                .updateTime(entity.getUpdateTime())
                .build();
    }
}
