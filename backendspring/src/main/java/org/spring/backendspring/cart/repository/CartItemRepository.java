package org.spring.backendspring.cart.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.spring.backendspring.cart.entity.CartItemEntity;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItemEntity, Long> {

    List<CartItemEntity> findByCartEntity_Id(Long cartId);

    Page<CartItemEntity> findByCartEntity_Id(Long cartId, Pageable pageable);

    Page<CartItemEntity> findByCartEntity_IdAndItemEntity_ItemTitleContainingIgnoreCase(
            Long cartId, String keyword, Pageable pageable
    );
}
