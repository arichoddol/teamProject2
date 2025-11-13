package org.spring.backendspring.cart.controller;

import lombok.RequiredArgsConstructor;
import org.spring.backendspring.cart.entity.CartEntity;
import org.spring.backendspring.cart.entity.CartItemEntity;
import org.spring.backendspring.cart.service.CartService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // 회원의 장바구니 조회
    @GetMapping("/{memberId}")
    public CartEntity getCart(@PathVariable("memberId") Long memberId) {
        return cartService.getCartByMemberId(memberId);
    }

    // 회원 장바구니 생성
    @PostMapping("/{memberId}")
    public CartEntity createCart(@PathVariable("memberId") Long memberId) {
        return cartService.createCart(memberId);
    }

    // 장바구니에 아이템 추가
    @PostMapping("/{cartId}/item")
    public CartItemEntity addItem(@PathVariable("cartId") Long cartId,
                                  @RequestParam("itemId") Long itemId,
                                  @RequestParam("itemSize") int itemSize) {
        return cartService.addItemToCart(cartId, itemId, itemSize);
    }

    // 장바구니 아이템 삭제
    @DeleteMapping("/item/{cartItemId}")
    public String removeItem(@PathVariable("cartItemId") Long cartItemId) {
        cartService.removeItem(cartItemId);
        return "삭제 완료";
    }
}
