package org.spring.backendspring.board.entity;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.spring.backendspring.board.dto.BoardReplyDto;
import org.spring.backendspring.common.BasicTime;
import org.spring.backendspring.member.entity.MemberEntity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "board_comment_tb" )
public class BoardReplyEntity extends BasicTime {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_comment_id" ,unique = true)
    private Long id;
 
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String content;


    // createTime 
    // updateTime 

    // N:1 
    @ManyToOne
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "board_id")
    @JsonIgnore
    private BoardEntity boardEntity;

    // N:1
    @ManyToOne
    @JoinColumn(name = "member_id")
    @JsonIgnore
    private MemberEntity memberEntity;


}
