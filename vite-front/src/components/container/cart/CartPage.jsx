import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import {
  getCartByMemberId,
  createCart,
  addItemToCart,
  removeCartItem,
} from "../../../apis/cart/cartApi";
import "../../../css/cart/CartPage.css";

export default function CartPage() {
  const { memberId: paramMemberId } = useParams();
  const memberId = paramMemberId ? Number(paramMemberId) : 1; // 없으면 1로 기본값
  const navigate = useNavigate();
  const [cart, setCart] = useState(null);
  const [loading, setLoading] = useState(true);

  // 상품 추가용
  const [itemId, setItemId] = useState("");
  const [itemSize, setItemSize] = useState(1);

  useEffect(() => {
    const fetchCart = async () => {
      try {
        const data = await getCartByMemberId(memberId);
        if (!data) {
          const newCart = await createCart(memberId);
          setCart(newCart);
        } else {
          setCart(data);
        }
      } catch (e) {
        console.error("장바구니 불러오기 실패:", e);
      } finally {
        setLoading(false);
      }
    };
    fetchCart();
  }, [memberId]);

  const handleAddItem = async () => {
    if (!cart) return alert("장바구니가 없습니다.");
    if (!itemId) return alert("상품 ID를 입력하세요.");

    try {
      const newItem = await addItemToCart(cart.id, itemId, itemSize);
      setCart((prev) => ({
        ...prev,
        cartItemEntities: [...(prev.cartItemEntities || []), newItem],
      }));
    } catch (e) {
      console.error("상품 추가 실패:", e);
    }
  };

  const handleRemoveItem = async (cartItemId) => {
    try {
      await removeCartItem(cartItemId);
      setCart((prev) => ({
        ...prev,
        cartItemEntities: prev.cartItemEntities.filter(
          (item) => item.cartItemId !== cartItemId
        ),
      }));
    } catch (e) {
      console.error("삭제 실패:", e);
    }
  };

  if (loading) return <p>로딩 중...</p>;
  if (!cart) return <p>장바구니가 없습니다.</p>;

  return (
    <div className="cartPage">
      <h1>장바구니 목록</h1>

      {/* 상품 추가 입력 영역 */}
      <div className="addItemBox">
        <input
          type="number"
          placeholder="상품 ID"
          value={itemId}
          onChange={(e) => setItemId(e.target.value)}
        />
        <input
          type="number"
          placeholder="사이즈"
          value={itemSize}
          onChange={(e) => setItemSize(e.target.value)}
        />
        <button onClick={handleAddItem}>장바구니 담기</button>
      </div>

      <table className="cartTable">
        <thead>
          <tr>
            <th>ID</th>
            <th>상품 ID</th>
            <th>사이즈</th>
            <th>회원 ID</th>
            <th>상품명</th>
            <th>상세설명</th>
            <th>가격</th>
            <th>생성일</th>
            <th>수정일</th>
            <th>삭제</th>
          </tr>
        </thead>
        <tbody>
          {cart.cartItemEntities?.length > 0 ? (
            cart.cartItemEntities.map((item) => (
              <tr key={item.cartItemId}>
                <td>{item.cartItemId}</td>
                <td>{item.itemEntity?.id}</td>
                <td>{item.itemSize}</td>
                <td>{cart.memberEntity?.id || memberId}</td>
                <td>{item.itemEntity?.itemTitle || "상품명 없음"}</td>
                <td>{item.itemEntity?.itemDetail || "-"}</td>
                <td>
                  {item.itemEntity?.itemPrice
                    ? item.itemEntity.itemPrice.toLocaleString()
                    : 0}
                  원
                </td>
                <td>{item.createTime || "-"}</td>
                <td>{item.updateTime || "-"}</td>
                <td>
                  <button
                    onClick={() => handleRemoveItem(item.cartItemId)}
                    className="deleteBtn"
                  >
                    삭제
                  </button>
                </td>
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan="10">장바구니에 상품이 없습니다.</td>
            </tr>
          )}
        </tbody>
      </table>

      {/* 하단 버튼 */}
      <div className="bottomLinks">
        <button onClick={() => navigate("/shop")}>상품 리스트</button>
        <button onClick={() => navigate(`/payment/${memberId}`)}>결제</button>
      </div>
    </div>
  );
}
