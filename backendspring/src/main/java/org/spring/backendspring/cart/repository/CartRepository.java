package org.spring.backendspring.cart.repository;

import org.spring.backendspring.cart.entity.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<CartEntity, Long> {
    Optional<CartEntity> findByMemberId(Long memberId);
}
