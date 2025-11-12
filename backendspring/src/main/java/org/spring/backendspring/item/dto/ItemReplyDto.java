package org.spring.backendspring.item.dto;

import java.time.LocalDateTime;

import org.spring.backendspring.item.entity.ItemEntity;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class ItemReplyDto {

    private Long id;

    private String title;
    private String content;

    // item_tb
    private Long itemId;
    // member_tb
    private Long memberId;


    private LocalDateTime createTime;
    private LocalDateTime updateTime;


    // N:1 
    // private MemberEntity memberEntity;

    private ItemEntity itemEntity;
    
}
