package org.spring.backendspring.crew.crewBoard.entity;

import jakarta.persistence.*;
import lombok.*;
import org.spring.backendspring.common.BasicTime;
import org.spring.backendspring.crew.crew.entity.CrewEntity;
import org.spring.backendspring.member.entity.MemberEntity;

import org.spring.backendspring.crew.crewBoard.dto.CrewBoardDto;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "crew_board_tb")
public class CrewBoardEntity extends BasicTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "crew_board_id")
    private Long id;

    @Column(nullable = false)
    private String title; // 제목

    @Column(nullable = false)
    private String content; // 내용

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crew_id", nullable = false)
    private CrewEntity crewEntity; // 소속 크루

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity memberEntity; // 작성자

    @OneToMany(mappedBy = "crewBoardEntity", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<CrewBoardCommentEntity> crewBoardCommentEntities; // 댓글

    @OneToMany(mappedBy = "crewBoardEntity", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<CrewBoardImageEntity> crewBoardImageEntities; // 댓글

    public static CrewBoardEntity toCrewBoardEntity(CrewBoardDto dto) {
        return CrewBoardEntity.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .crewEntity(dto.getCrewEntity())
                .memberEntity(dto.getMemberEntity())
                .crewBoardCommentEntities(dto.getCrewBoardCommentEntities())
                .crewBoardImageEntities(dto.getCrewBoardImageEntities())
                .build();
    }
}