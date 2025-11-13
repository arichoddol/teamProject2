package org.spring.backendspring.cart.service.impl;

import lombok.RequiredArgsConstructor;
import org.spring.backendspring.cart.entity.CartEntity;
import org.spring.backendspring.cart.entity.CartItemEntity;
import org.spring.backendspring.cart.repository.CartRepository;
import org.spring.backendspring.cart.repository.CartItemRepository;
import org.spring.backendspring.cart.service.CartService;
import org.spring.backendspring.item.entity.ItemEntity;
import org.spring.backendspring.item.repository.ItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ItemRepository itemRepository; // Item 조회용

    @Override
    public CartEntity getCartByMemberId(Long memberId) {
        return cartRepository.findByMemberId(memberId).orElse(null);
    }

    @Override
    public CartEntity createCart(Long memberId) {
        return cartRepository.findByMemberId(memberId)
                .orElseGet(() -> cartRepository.save(CartEntity.builder()
                        .memberId(memberId)
                        .build()));
    }

    @Override
    public CartItemEntity addItemToCart(Long cartId, Long itemId, int itemSize) {
        CartEntity cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("장바구니를 찾을 수 없습니다!"));

        ItemEntity itemEntity = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다!"));

        CartItemEntity cartItem = CartItemEntity.builder()
                .cartEntity(cart)
                .itemSize(itemSize)
                .itemEntity(itemEntity) // 연관 관계 설정
                .build();

        return cartItemRepository.save(cartItem);
    }

    @Override
    public List<CartItemEntity> getCartItems(Long cartId) {
        // itemEntity 정보까지 lazy-loading 되도록
        return cartItemRepository.findByCartEntity_Id(cartId);
    }

    @Override
    public void removeItem(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }
}
