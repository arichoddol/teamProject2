package org.spring.backendspring.cart.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.spring.backendspring.cart.dto.CartDto;
import org.spring.backendspring.common.BasicTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "cart_tb")
public class CartEntity extends BasicTime{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_Id")
    private Long id;

    
    private Long memberId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    // 1:N
    @OneToMany(mappedBy = "cartEntity", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<CartItemEntity> cartItemEntities = new ArrayList<>();

     // --- DTO 변환 메서드 추가 ---
    
    public CartDto toDto() {
        return CartDto.builder()
                .cartId(this.id)
                .memberId(this.memberId)
                .createTime(this.createTime)
                .updateTime(this.updateTime)
                .items(this.cartItemEntities != null
                        ? this.cartItemEntities.stream()
                            .map(CartItemEntity::toDto)
                            .collect(Collectors.toList())
                        : new ArrayList<>())
                .build();
    }

    public static CartEntity fromDto(CartDto dto) {
        if (dto == null) return null;

        CartEntity cart = CartEntity.builder()
                .id(dto.getCartId())
                .memberId(dto.getMemberId())
                .createTime(dto.getCreateTime())
                .updateTime(dto.getUpdateTime())
                .build();

        if (dto.getItems() != null) {
            List<CartItemEntity> itemEntities = dto.getItems().stream()
                    .map(itemDto -> {
                        CartItemEntity itemEntity = CartItemEntity.fromDto(itemDto);
                        itemEntity.setCartEntity(cart); // 양방향
                        return itemEntity;
                    })
                    .collect(Collectors.toList());
            cart.setCartItemEntities(itemEntities);
        }

        return cart;
    }
}