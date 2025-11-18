import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { getCartByMemberId } from "../../../apis/cart/cartApi"; 
import { pgRequest } from "../../../apis/payment/paymentApi"; 
import "../../../css/payment/PaymentPage.css";

const PaymentPage = () => {
  const navigate = useNavigate();
  const [cart, setCart] = useState(null);
  const [loading, setLoading] = useState(true);
  const [paymentType, setPaymentType] = useState("kakao");

  useEffect(() => {
    const fetchCart = async () => {
      try {
        const data = await getCartByMemberId();
        setCart(data?.items?.length ? data : null);
      } catch (e) {
        console.error("장바구니 불러오기 실패:", e);
        alert("로그인이 필요합니다.");
        navigate("/auth/login");
      } finally {
        setLoading(false);
      }
    };
    fetchCart();
  }, [navigate]);

  const handlePayment = async () => {
    if (!cart || !cart.items?.length) {
      alert("장바구니에 상품이 없습니다.");
      return;
    }

    try {
      const firstItem = cart.items[0];
      const productId = firstItem.itemId;
      const productName = firstItem.itemTitle || "상품";
      const totalPrice = cart.items.reduce(
        (sum, item) => sum + (item.itemPrice || 0) * (item.quantity || 1),
        0
      );

      const memberId = cart.memberId;
      if (!memberId) throw new Error("회원 정보가 없습니다.");

      if (paymentType === "kakao") {
        const approvalUrl = await pgRequest("kakao", productId, totalPrice, productName, memberId);
        window.location.href = approvalUrl; // 카카오 결제 페이지 이동
      } else {
        alert("현금/카드 결제는 아직 지원되지 않습니다.");
      }
    } catch (e) {
      console.error("결제 실패:", e);
      alert("결제 실패: " + e.message);
    }
  };

  if (loading) return <p>로딩 중...</p>;
  if (!cart || !cart.items?.length) return <p>장바구니에 상품이 없습니다.</p>;

  const totalPrice = cart.items.reduce(
    (sum, item) => sum + (item.itemPrice || 0) * (item.quantity || 1),
    0
  );

  return (
    <div className="payment">
      <h1>결제목록</h1>
      {cart.items.map((item) => (
        <div key={item.cartItemId}>
          {item.itemTitle} - {item.itemPrice.toLocaleString()}원 x {item.quantity || 1}개
        </div>
      ))}
      <div>총 결제금액: {totalPrice.toLocaleString()}원</div>

      <div>
        결제방법:
        <select value={paymentType} onChange={(e) => setPaymentType(e.target.value)}>
          <option value="kakao">Kakao Pay</option>
          <option value="card">카드</option>
          <option value="현금">현금</option>
        </select>
      </div>

      <button onClick={handlePayment}>결제하기</button>
    </div>
  );
};

export default PaymentPage;
