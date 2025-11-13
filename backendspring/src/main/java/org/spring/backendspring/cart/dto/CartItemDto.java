package org.spring.backendspring.cart.dto;

import lombok.*;
import org.spring.backendspring.cart.entity.CartItemEntity;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemDto {

    private Long cartItemId;
    private Long itemId;
    private int itemSize;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    //Entity → DTO 변환
    public static CartItemDto fromEntity(CartItemEntity entity) {
        if (entity == null) return null;

        return CartItemDto.builder()
                .cartItemId(entity.getCartItemId())
                .itemId(entity.getItemId())
                .itemSize(entity.getItemSize())
                .createTime(entity.getCreateTime())
                .updateTime(entity.getUpdateTime())
                .build();
    }

    //DTO → Entity 변환
    public CartItemEntity toEntity() {
        return CartItemEntity.builder()
                .cartItemId(this.cartItemId)
                .itemId(this.itemId)
                .itemSize(this.itemSize)
                .createTime(this.createTime)
                .updateTime(this.updateTime)
                .build();
    }
}
