package org.spring.backendspring.cart.dto;

import lombok.Builder;
import lombok.Getter;
import org.spring.backendspring.cart.entity.CartItemEntity;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class CartDto {

    private Long cartId;
    private Long memberId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private List<CartItemEntity> items;
}

