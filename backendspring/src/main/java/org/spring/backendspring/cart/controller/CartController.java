package org.spring.backendspring.cart.controller;

import lombok.RequiredArgsConstructor;
import org.spring.backendspring.cart.entity.CartEntity;
import org.spring.backendspring.cart.entity.CartItemEntity;
import org.spring.backendspring.cart.service.CartService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping("/{memberId}")
    public CartEntity getCart(@PathVariable Long memberId) {
        return cartService.getCartByMemberId(memberId);
    }

    @PostMapping("/{memberId}")
    public CartEntity createCart(@PathVariable Long memberId) {
        return cartService.createCart(memberId);
    }

    @PostMapping("/{cartId}/item")
    public CartItemEntity addItem(@PathVariable Long cartId,
                                  @RequestParam Long itemId,
                                  @RequestParam int itemSize) {
        return cartService.addItemToCart(cartId, itemId, itemSize);
    }

//    @GetMapping("/{cartId}/items")
//    public List<CartItemEntity> getItems(@PathVariable Long cartId) {
//        return cartService.getCartItems(cartId);
//    }

    @DeleteMapping("/item/{cartItemId}")
    public String removeItem(@PathVariable Long cartItemId) {
        cartService.removeItem(cartItemId);
        return "삭제 완료";
    }
}
