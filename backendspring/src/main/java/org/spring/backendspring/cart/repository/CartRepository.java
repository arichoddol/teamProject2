package org.spring.backendspring.cart.repository;

import org.spring.backendspring.cart.entity.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CartRepository extends JpaRepository<CartEntity, Long> {
    Optional<CartEntity> findByMemberId(Long memberId);

    // CartRepository.java
    @Query("SELECT c FROM CartEntity c JOIN FETCH c.cartItemEntities WHERE c.memberId = :memberId")
    Optional<CartEntity> findByMemberIdWithItems(@Param("memberId") Long memberId);

    @Query("SELECT c FROM CartEntity c JOIN FETCH c.cartItemEntities ci JOIN FETCH ci.itemEntity i LEFT JOIN FETCH i.itemImgEntities WHERE c.id = :cartId")
    Optional<CartEntity> findByIdWithItems(@Param("cartId") Long cartId);
}
