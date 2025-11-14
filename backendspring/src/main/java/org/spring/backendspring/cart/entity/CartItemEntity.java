package org.spring.backendspring.cart.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import org.spring.backendspring.cart.dto.CartItemDto;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "cart_item_tb")
public class CartItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartItemId;

    private Long itemId;

    private int itemSize;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private CartEntity cartEntity;

     // --- DTO 변환 메서드 추가 ---
    
    public CartItemDto toDto() {
        return CartItemDto.builder()
                .cartItemId(this.cartItemId)
                .itemId(this.itemId)
                .itemSize(this.itemSize)
                .createTime(this.createTime)
                .updateTime(this.updateTime)
                .build();
    }

    public static CartItemEntity fromDto(CartItemDto dto) {
        if (dto == null) return null;

        return CartItemEntity.builder()
                .cartItemId(dto.getCartItemId())
                .itemId(dto.getItemId())
                .itemSize(dto.getItemSize())
                .createTime(dto.getCreateTime())
                .updateTime(dto.getUpdateTime())
                .build();
    }
}