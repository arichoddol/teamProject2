package org.spring.backendspring.item.dto;

import java.time.LocalDateTime;

import org.spring.backendspring.item.entity.ItemEntity;

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
public class ItemImgDto {

    private Long id;

    private Long itemId;

    private String oldName;
    private String newName;

    // itemEntityId 
    private Long itemEntityId;

  
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    

    // N:1
    private ItemEntity itemEntity;

    
}
