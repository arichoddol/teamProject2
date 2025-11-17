import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { getCartByMemberId } from "../../../apis/cart/cartApi"; // 장바구니 API
import { pgRequest } from "../../../apis/payment/paymentApi"; // Kakao 결제 API
import "../../../css/payment/PaymentPage.css";

const PaymentPage = () => {
  const { memberId } = useParams();
  const [cart, setCart] = useState(null);
  const [loading, setLoading] = useState(true);
  const [paymentType, setPaymentType] = useState("kakao");
  const [orderPost, setOrderPost] = useState("상계지점");
  const [orderMethod, setOrderMethod] = useState("배달");
  const [orderAddr, setOrderAddr] = useState("");

  useEffect(() => {
    const fetchCart = async () => {
      try {
        const data = await getCartByMemberId(Number(memberId));
        if (!data || !data.cartItemEntities?.length) {
          // 임시 더미 상품
          setCart({
            memberEntity: { id: memberId },
            cartItemEntities: [
              {
                cartItemId: 1,
                itemSize: 2,
                itemEntity: {
                  id: 101,
                  itemTitle: "테스트 상품 1",
                  itemDetail: "임시 상품",
                  itemPrice: 5000,
                },
                createTime: "2025-11-12",
                updateTime: "2025-11-12",
              },
            ],
          });
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

  const handlePayment = async () => {
    try {
      const totalPrice = cart.cartItemEntities.reduce(
        (sum, item) => sum + (item.itemEntity?.itemPrice || 0) * item.itemSize,
        0
      );

      if (paymentType === "kakao") {
        const approvalUrl = await pgRequest(
          "kakao",
          cart.cartItemEntities[0]?.itemEntity?.id || 1, // 상품 ID
          Number(memberId),
          totalPrice,
          "테스트 상품"
        );
        window.location.href = approvalUrl; // 카카오 결제 페이지로 이동
      } else {
        alert("현금/카드 결제는 아직 구현되지 않았습니다.");
      }
    } catch (e) {
      console.error("결제 요청 실패:", e);
      alert("결제 실패");
    }
  };

  if (loading) return <p>로딩 중...</p>;
  if (!cart || !cart.cartItemEntities?.length) return <p>장바구니에 상품이 없습니다.</p>;

  const totalPrice = cart.cartItemEntities.reduce(
    (sum, item) => sum + (item.itemEntity?.itemPrice || 0) * item.itemSize,
    0
  );

  return (
    <div className="payment">
      <h1>결제목록</h1>
      {cart.cartItemEntities.map((item) => (
        <div key={item.cartItemId}>
          <span>{item.itemEntity?.itemTitle}</span> -{" "}
          <span>{(item.itemEntity?.itemPrice || 0).toLocaleString()}원</span> x{" "}
          {item.itemSize}개
        </div>
      ))}
      <div>총 결제금액: {totalPrice.toLocaleString()}원</div>
      <div>
        결제방법:
        <select value={paymentType} onChange={(e) => setPaymentType(e.target.value)}>
          <option value="kakao">Kakao Pay</option>
          <option value="현금">현금</option>
          <option value="card">카드</option>
        </select>
      </div>
      <div>
        주문처:
        <select value={orderPost} onChange={(e) => setOrderPost(e.target.value)}>
          <option value="상계지점">상계지점</option>
          <option value="중계지점">중계지점</option>
          <option value="하계지점">하계지점</option>
        </select>
      </div>
      <div>
        주문방법:
        <select value={orderMethod} onChange={(e) => setOrderMethod(e.target.value)}>
          <option value="배달">배달</option>
          <option value="택배">택배</option>
          <option value="직접">직접</option>
        </select>
      </div>
      <div>
        배달주소:
        <input value={orderAddr} onChange={(e) => setOrderAddr(e.target.value)} />
      </div>
      <button onClick={handlePayment}>결제하기</button>
    </div>
  );
};

export default PaymentPage;
