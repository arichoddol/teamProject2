package org.spring.backendspring.cart.repository;

import org.spring.backendspring.cart.entity.CartItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItemEntity, Long> {
//    List<CartItemEntity> findByCart_CartId(Long cartId);
}