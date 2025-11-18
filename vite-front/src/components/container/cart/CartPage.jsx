import React, { useEffect, useState } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import {
  getCartByMemberId,
  createCart,
  addItemToCart,
  removeCartItem,
} from "../../../apis/cart/cartApi";
import "../../../css/cart/CartPage.css";

export default function CartPage() {
  const navigate = useNavigate();
  const location = useLocation();
  const itemToAdd = location.state?.itemToAdd;

  const [cart, setCart] = useState(null);
  const [loading, setLoading] = useState(true);

  // 장바구니 조회 또는 생성
  const fetchCart = async () => {
    setLoading(true);
    try {
      let data = await getCartByMemberId();
      if (!data) data = await createCart();
      setCart(data);
    } catch (e) {
      console.error("장바구니 불러오기 실패:", e);
      alert("로그인이 필요합니다.");
      navigate("/auth/login", { state: { from: location, itemToAdd } });
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchCart();
  }, []);

  // 전달된 상품 자동 추가
  useEffect(() => {
    if (!cart || !itemToAdd) return;

    const addItem = async () => {
      try {
        const newItem = await addItemToCart(cart.cartId, itemToAdd.id, 1);
        setCart(prev => ({
          ...prev,
          items: [...(prev.items || []), newItem],
        }));
        navigate("/cart", { replace: true }); // state 초기화
      } catch (e) {
        console.error("상품 추가 실패:", e);
      }
    };

    addItem();
  }, [cart, itemToAdd, navigate]);

  const handleRemoveItem = async (cartItemId) => {
    try {
      await removeCartItem(cartItemId);
      setCart(prev => ({
        ...prev,
        items: prev.items.filter(item => item.cartItemId !== cartItemId),
      }));
    } catch (e) {
      console.error("삭제 실패:", e);
    }
  };

  if (loading) return <p>로딩 중...</p>;
  if (!cart || !cart.items?.length) return <p>장바구니에 상품이 없습니다.</p>;

  return (
    <div className="cartPage">
      <h1>장바구니 목록</h1>
      <table className="cartTable">
        <thead>
          <tr>
            <th>ID</th>
            <th>상품명</th>
            <th>가격</th>
            <th>삭제</th>
          </tr>
        </thead>
        <tbody>
          {cart.items.map(item => (
            <tr key={item.cartItemId}>
              <td>{item.cartItemId}</td>
              <td>{item.itemTitle || "상품명 없음"}</td>
              <td>{item.itemPrice.toLocaleString()}원</td>
              <td>
                <button onClick={() => handleRemoveItem(item.cartItemId)}>삭제</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      <div className="bottomLinks">
        <button onClick={() => navigate("/shop")}>상품 리스트</button>
        <button onClick={() => navigate("/payment")}>결제</button>
      </div>
    </div>
  );
}
