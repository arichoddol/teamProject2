package org.spring.backendspring.board.entity;

import java.util.List;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.spring.backendspring.board.dto.BoardDto;
import org.spring.backendspring.common.BasicTime;
import org.spring.backendspring.member.entity.MemberEntity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "board_tb" )
public class BoardEntity extends BasicTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id", unique = true)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String content;


    private int attachFile;
    private int hit;

    // createTime
    // updateTime 

    // N:1
    @ManyToOne
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name ="member_id")
    private MemberEntity memberEntity;

    // 1:N
    @OneToMany( mappedBy = "boardEntity",
        fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<BoardReplyEntity> boardReplyEntities;

    @OneToMany( mappedBy = "boardEntity",
        fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<BoardImgEntity> boardImgEntities;

    

    // toEntity
    public static BoardEntity toBoardEntity(BoardDto boardDto ) {
        // builder()
        int attachFileValue = boardDto.getBoardFile() != null && 
                                !boardDto.getBoardFile().isEmpty() ? 1:0;
        
        return BoardEntity.builder()
                        .id(boardDto.getId())
                        .title(boardDto.getTitle())
                        .content(boardDto.getContent())
                        .hit(boardDto.getHit())
                        .memberEntity(boardDto.getMemberentity())
                        .attachFile(attachFileValue)
                        .build();
    }
    
}
