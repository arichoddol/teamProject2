package org.spring.backendspring.cart.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.spring.backendspring.common.BasicTime;

@Entity
@Getter
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

}
