package org.spring.backendspring.checkout.service.impl;

import lombok.RequiredArgsConstructor;
import org.spring.backendspring.cart.entity.CartEntity;
import org.spring.backendspring.cart.entity.CartItemEntity;
import org.spring.backendspring.cart.repository.CartRepository;
import org.spring.backendspring.checkout.service.CheckoutService;
import org.spring.backendspring.payment.entity.PaymentEntity;
import org.spring.backendspring.payment.entity.PaymentItemEntity;
import org.spring.backendspring.payment.repository.PaymentRepository;
import org.spring.backendspring.payment.repository.PaymentItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class CheckoutServiceImpl implements CheckoutService {

 private final CartRepository cartRepository;
 private final PaymentRepository paymentRepository;
 private final PaymentItemRepository paymentItemRepository;

 @Override
 public PaymentEntity checkoutCart(Long cartId, String paymentAddr, String paymentMethod) {
 // 1️⃣ 장바구니 조회: N+1 문제 해결을 위해 Fetch Join 메서드 사용
 // ❌ 기존: CartEntity cart = cartRepository.findById(cartId)
 // ✅ 수정: findByIdWithItems를 사용하여 CartItems 및 ItemEntity까지 미리 로드
 CartEntity cart = cartRepository.findByIdWithItems(cartId) // ⭐️ 이 부분만 수정하면 됩니다.
.orElseThrow(() -> new RuntimeException("장바구니를 찾을 수 없습니다."));

 // 2️⃣ 결제 정보 생성
 PaymentEntity payment = PaymentEntity.builder()
 .memberId(cart.getMemberId())
 .paymentAddr(paymentAddr)
 .paymentMethod(paymentMethod)
 .paymentResult("READY")
 .paymentType("CARD")
 .build();

 PaymentEntity savedPayment = paymentRepository.save(payment);

 // 3️⃣ 장바구니 상품 → 결제상품으로 변환
 // Fetch Join 덕분에 이 반복문 내에서 추가적인 쿼리 (N+1)가 발생하지 않습니다.
 for (CartItemEntity cartItem : cart.getCartItemEntities()) {
 PaymentItemEntity paymentItem = PaymentItemEntity.builder()
 .payment(savedPayment)
 .itemId(cartItem.getItemEntity() != null ? cartItem.getItemEntity().getId() : null)
 .size(cartItem.getItemSize())
 .price(cartItem.getItemEntity() != null ? cartItem.getItemEntity().getItemPrice() : 0)
.title(cartItem.getItemEntity() != null ? cartItem.getItemEntity().getItemTitle() : "상품")
.s3file(cartItem.getItemEntity() != null && !cartItem.getItemEntity().getItemImgEntities().isEmpty()
 ? cartItem.getItemEntity().getItemImgEntities().get(0).getNewName()
: null)
 .createTime(LocalDateTime.now())
 .updateTime(LocalDateTime.now())
.build();

 paymentItemRepository.save(paymentItem);
 }

 return savedPayment;
 }
}