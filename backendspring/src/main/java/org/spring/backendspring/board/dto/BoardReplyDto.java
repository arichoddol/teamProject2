package org.spring.backendspring.board.dto;

import java.time.LocalDateTime;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.spring.backendspring.board.entity.BoardEntity;
import org.spring.backendspring.board.entity.BoardReplyEntity;
import org.spring.backendspring.member.entity.MemberEntity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardReplyDto {

    private Long id;
    private Long boardId;
    private Long memberId;

    private String title;
    private String content;


    // createTime 
    private LocalDateTime createTime;
    // updateTime 
    private LocalDateTime upDateTime;


    // N:1 
    private BoardEntity boardEntity;

    // N:1
    private MemberEntity memberEntity;

    
}
