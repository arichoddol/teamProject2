package org.spring.backendspring.cart.controller;

import lombok.RequiredArgsConstructor;
import org.spring.backendspring.cart.dto.CartDto;
import org.spring.backendspring.cart.dto.CartItemDto;
import org.spring.backendspring.cart.entity.CartEntity;
import org.spring.backendspring.cart.entity.CartItemEntity;
import org.spring.backendspring.cart.service.CartService;
import org.spring.backendspring.common.dto.PagedResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    // 장바구니 아이템 페이징 조회
    @GetMapping("/{cartId}/items")
    public PagedResponse<CartItemDto> getCartItems(
            @PathVariable("cartId") Long cartId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String keyword // ✅ 검색 키워드
    ) {
        Page<CartItemEntity> pageResult;

        if (keyword.isEmpty()) {
            pageResult = cartService.getCartItems(cartId, PageRequest.of(page, size));
        } else {
            pageResult = cartService.searchCartItems(cartId, keyword, PageRequest.of(page, size));
        }

        List<CartItemDto> dtos = pageResult.getContent()
                .stream()
                .map(CartItemEntity::toDto)
                .toList();

        return PagedResponse.<CartItemDto>builder()
                .content(dtos)
                .currentPage(pageResult.getNumber() + 1)
                .totalPages(pageResult.getTotalPages())
                .totalElements(pageResult.getTotalElements())
                .hasNext(pageResult.hasNext())
                .hasPrevious(pageResult.hasPrevious())
                .hasFirst(pageResult.getNumber() + 1 > 1)
                .hasLast(pageResult.getNumber() + 1 < pageResult.getTotalPages())
                .startPage(((pageResult.getNumber()) / 5) * 5 + 1)
                .endPage(Math.min(((pageResult.getNumber()) / 5) * 5 + 5, pageResult.getTotalPages()))
                .blockSize(5)
                .build();
    }

}
