package org.spring.backendspring.cart.controller;

import lombok.RequiredArgsConstructor;
import org.spring.backendspring.cart.dto.CartDto;
import org.spring.backendspring.cart.dto.CartItemDto;
import org.spring.backendspring.cart.entity.CartEntity;
import org.spring.backendspring.cart.service.CartService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // 회원 장바구니 조회 또는 생성
    @GetMapping("/{memberId}")
    public CartDto getOrCreateCart(@PathVariable("memberId") Long memberId) {
        CartEntity cart = cartService.getCartByMemberId(memberId);
        if (cart == null) {
            cart = cartService.createCart(memberId);
        }
        return cart.toDto();
    }

    // 장바구니에 아이템 추가
    @PostMapping("/{cartId}/item")
    public CartItemDto addItem(@PathVariable("cartId") Long cartId,
            @RequestParam("itemId") Long itemId,
            @RequestParam("itemSize") int itemSize) {
        return cartService.addItemToCart(cartId, itemId, itemSize).toDto();
    }

    // 장바구니 아이템 삭제
    @DeleteMapping("/item/{cartItemId}")
    public String removeItem(@PathVariable("cartItemId") Long cartItemId) {
        cartService.removeItem(cartItemId);
        return "삭제 완료";
    }
}
