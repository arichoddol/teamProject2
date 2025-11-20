package org.spring.backendspring.cart.service.impl;

import lombok.RequiredArgsConstructor;
import org.spring.backendspring.cart.entity.CartEntity;
import org.spring.backendspring.cart.entity.CartItemEntity;
import org.spring.backendspring.cart.repository.CartRepository;
import org.spring.backendspring.cart.repository.CartItemRepository;
import org.spring.backendspring.cart.service.CartService;
import org.spring.backendspring.item.entity.ItemEntity;
import org.spring.backendspring.item.repository.ItemRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public CartEntity getCartByMemberId(Long memberId) {
        CartEntity cart = cartRepository.findByMemberId(memberId).orElse(null);
        if (cart != null) {
            cart.getCartItemEntities().size(); // 강제 로딩
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

    @Override
    public Page<CartItemEntity> getCartItems(Long cartId, Pageable pageable) {
        return cartItemRepository.findByCartEntity_Id(cartId, pageable);
    }

    @Override
    public Page<CartItemEntity> searchCartItems(Long cartId, String keyword, Pageable pageable) {
        return cartItemRepository.findByCartEntity_IdAndItemEntity_ItemTitleContainingIgnoreCase(
                cartId, keyword, pageable);
    }

    // 수량 변경 구현
    @Override
    public CartItemEntity updateItemQuantity(Long cartItemId, int quantity) {
        CartItemEntity cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("장바구니 아이템을 찾을 수 없습니다: " + cartItemId));

        cartItem.setItemSize(quantity); 

        return cartItemRepository.save(cartItem); 
    }
}
