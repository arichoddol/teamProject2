package org.spring.backendspring.cart.service.impl;

import lombok.RequiredArgsConstructor;
import org.spring.backendspring.cart.entity.CartEntity;
import org.spring.backendspring.cart.entity.CartItemEntity;
import org.spring.backendspring.cart.repository.CartRepository;
import org.spring.backendspring.cart.repository.CartItemRepository;
import org.spring.backendspring.cart.service.CartService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    public CartEntity getCartByMemberId(Long memberId) {
        return cartRepository.findByMemberId(memberId).orElse(null);
    }

    @Override
    public CartEntity createCart(Long memberId) {
        CartEntity cart = CartEntity.builder()
                .memberId(memberId)
                .build();
        return cartRepository.save(cart);
    }

    @Override
    public CartItemEntity addItemToCart(Long cartId, Long itemId, int itemSize) {
        CartEntity cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("장바구니를 찾을수 없습니다!!"));

        CartItemEntity item = CartItemEntity.builder()
                .cartEntity(cart)
                .itemId(itemId)
                .itemSize(itemSize)
                .build();

        return cartItemRepository.save(item);
    }

//    @Override
//    public List<CartItemEntity> getCartItems(Long cartId) {
//        return cartItemRepository.findByCart_CartId(cartId);
//    }

    @Override
    public void removeItem(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }
}
