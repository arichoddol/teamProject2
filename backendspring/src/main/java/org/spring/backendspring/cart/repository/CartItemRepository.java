package org.spring.backendspring.cart.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying; 
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.spring.backendspring.cart.entity.CartEntity;
import org.spring.backendspring.cart.entity.CartItemEntity;

import java.util.List;
import java.util.Optional; // ⭐️ Optional 임포트 추가

public interface CartItemRepository extends JpaRepository<CartItemEntity, Long> {

    List<CartItemEntity> findByCartEntity_Id(Long cartId);

    Page<CartItemEntity> findByCartEntity_Id(Long cartId, Pageable pageable);

    Page<CartItemEntity> findByCartEntity_IdAndItemEntity_ItemTitleContainingIgnoreCase(
            Long cartId, String keyword, Pageable pageable);

    @Query("SELECT ci FROM CartItemEntity ci " +
        "JOIN FETCH ci.itemEntity i " +          // ItemEntity 즉시 로딩
        "JOIN FETCH i.itemImgEntities " +        // ItemImgEntities 즉시 로딩
        "WHERE ci.cartEntity.id = :cartId")
    Page<CartItemEntity> findCartItemsWithImagesByCartId(@Param("cartId") Long cartId, Pageable pageable);

    @Query("SELECT ci FROM CartItemEntity ci " +
           "JOIN FETCH ci.itemEntity i " +
           "JOIN FETCH i.itemImgEntities " +
           "WHERE ci.cartEntity.id = :cartId AND i.itemTitle LIKE %:keyword%")
    Page<CartItemEntity> searchCartItemsWithImages(@Param("cartId") Long cartId, @Param("keyword") String keyword, Pageable pageable);

    // ⭐️⭐️ [핵심 추가] 동일 상품 존재 여부 확인 메서드 ⭐️⭐️
    /**
     * 특정 장바구니 내에서 특정 상품 ID를 가진 CartItemEntity를 조회합니다.
     * (동일 상품 추가 시 수량 누적 로직에 사용됨)
     */
    Optional<CartItemEntity> findByCartEntity_IdAndItemEntity_Id(Long cartId, Long itemId);

    // 기존 메서드
    List<CartItemEntity> findByCartEntityAndItemEntity_IdIn(CartEntity cartEntity, List<Long> itemIds);

    // ⭐️ [추가] CartItem과 ItemEntity를 JOIN FETCH하여 N+1 문제 해결
    @Query("SELECT ci FROM CartItemEntity ci JOIN FETCH ci.itemEntity WHERE ci.cartEntity = :cartEntity AND ci.itemEntity.id IN :itemIds")
    List<CartItemEntity> findByCartEntityAndItemEntity_IdInWithItem(
            @Param("cartEntity") CartEntity cartEntity,
            @Param("itemIds") List<Long> itemIds);

    // *************************************************************************
    // 💥 핵심 추가 부분: 장바구니 ID를 이용한 CartItem 전체 벌크 삭제
    // *************************************************************************

    /**
     * 특정 Cart ID에 해당하는 모든 CartItemEntity를 DB에서 직접 삭제합니다.
     * 이 메서드는 PaymentServiceImpl.removeCartByMemberId()에서 호출됩니다.
     * @param cartId 삭제할 CartEntity의 ID
     */
    @Modifying // DML 쿼리(INSERT, UPDATE, DELETE)임을 명시
    @Query("DELETE FROM CartItemEntity ci WHERE ci.cartEntity.id = :cartId")
    void deleteByCartId(@Param("cartId") Long cartId);
}