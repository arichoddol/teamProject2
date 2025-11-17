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
    @Transactional
    public CartEntity getCartByMemberId(Long memberId) {
        CartEntity cart = cartRepository.findByMemberId(memberId).orElse(null);
        if (cart != null) {
            cart.getCartItemEntities().size(); // 강제로 로딩
        }
        return cart;
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
                .itemEntity(itemEntity)
                .build();

        CartItemEntity savedItem = cartItemRepository.save(cartItem);

        // CartEntity 내부 리스트에도 추가
        cart.getCartItemEntities().add(savedItem);

        return savedItem;
    }

    @Override
    public List<CartItemEntity> getCartItems(Long cartId) {
        return cartItemRepository.findByCartEntity_Id(cartId);
    }

    @Override
    public void removeItem(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }
}
