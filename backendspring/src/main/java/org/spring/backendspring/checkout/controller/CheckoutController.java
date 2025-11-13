package org.spring.backendspring.checkout.controller;

import lombok.RequiredArgsConstructor;
import org.spring.backendspring.checkout.service.CheckoutService;
import org.spring.backendspring.payment.entity.PaymentEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/checkout")
@RequiredArgsConstructor
public class CheckoutController {

    private final CheckoutService checkoutService;

    @PostMapping("/{cartId}")
    public PaymentEntity checkoutCart(
            @PathVariable Long cartId,
            @RequestParam String paymentAddr,
            @RequestParam String paymentMethod) {

        return checkoutService.checkoutCart(cartId, paymentAddr, paymentMethod);
    }
}
