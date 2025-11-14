package org.spring.backendspring.cart.service;

import org.spring.backendspring.cart.entity.CartEntity;
import org.spring.backendspring.cart.entity.CartItemEntity;

import java.util.List;

public interface CartService {
    
    CartEntity getCartByMemberId(Long memberId);
    CartEntity createCart(Long memberId);
    CartItemEntity addItemToCart(Long cartId, Long itemId, int itemSize);
    List<CartItemEntity> getCartItems(Long cartId);
    void removeItem(Long cartItemId);
}
