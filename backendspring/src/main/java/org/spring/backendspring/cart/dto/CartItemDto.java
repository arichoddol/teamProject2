package org.spring.backendspring.cart.dto;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
public class CartItemDto {
    
    private Long cartItemId;
    private Long itemId;
    private int itemSize;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
