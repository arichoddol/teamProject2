import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { getCartByMemberId } from "../../../apis/cart/cartApi"; // 장바구니 API
import { createPayment } from "../../../apis/payment/paymentApi";
import "../../../css/payment/PaymentPage.css";

const PaymentPage = () => {
  const { memberId } = useParams(); // URL에서 memberId 가져오기
  const navigate = useNavigate(); // 페이지 이동 훅
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
                  itemDetail: "임시로 만든 상품",
                  itemPrice: 5000,
                },
                createTime: "2025-11-12",
                updateTime: "2025-11-12",
              },
              {
                cartItemId: 2,
                itemSize: 1,
                itemEntity: {
                  id: 102,
                  itemTitle: "테스트 상품 2",
                  itemDetail: "임시로 만든 상품",
                  itemPrice: 12000,
                },
                createTime: "2025-11-12",
                updateTime: "2025-11-12",
              },
            ],
          });
        } else {
          setCart(data); // 실제 장바구니 있으면 그대로
        }
      } catch (e) {
        console.error("장바구니 불러오기 실패:", e);
        // 실패시에도 임시 데이터
        setCart({
          memberEntity: { id: memberId },
          cartItemEntities: [
            {
              cartItemId: 1,
              itemSize: 2,
              itemEntity: {
                id: 101,
                itemTitle: "테스트 상품 1",
                itemDetail: "임시로 만든 상품",
                itemPrice: 5000,
              },
              createTime: "2025-11-12",
              updateTime: "2025-11-12",
            },
          ],
        });
      } finally {
        setLoading(false);
      }
    };
    fetchCart();
  }, [memberId]);

  const handlePayment = async () => {
    try {
      const res = await createPayment({
        memberId,
        paymentType,
        orderPost,
        orderMethod,
        orderAddr,
        paymentResult: "SUCCESS",
      });
      alert(`결제 완료 (id: ${res.paymentId})`);
      navigate("/payment/success"); //결제 후 페이지 이동
    } catch (e) {
      console.error(e);
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
      <div className="payment-con">
        <h1>결제목록</h1>
        <table>
          <thead>
            <tr>
              <th>ID</th>
              <th>상품ID</th>
              <th>회원ID</th>
              <th>상품명</th>
              <th>상세설명</th>
              <th>가격</th>
              <th>수량</th>
              <th>합계</th>
              <th>생성일</th>
              <th>수정일</th>
            </tr>
          </thead>
          <tbody>
            {cart.cartItemEntities.map((item) => (
              <tr key={item.cartItemId}>
                <td>{item.cartItemId}</td>
                <td>{item.itemEntity?.id}</td>
                <td>{cart.memberEntity?.id || memberId}</td>
                <td>{item.itemEntity?.itemTitle}</td>
                <td>{item.itemEntity?.itemDetail}</td>
                <td>{item.itemEntity?.itemPrice?.toLocaleString() || 0}원</td>
                <td>{item.itemSize}</td>
                <td>{((item.itemEntity?.itemPrice || 0) * item.itemSize).toLocaleString()}원</td>
                <td>{item.createTime || "-"}</td>
                <td>{item.updateTime || "-"}</td>
              </tr>
            ))}
            <tr>
              <td colSpan="10">
                <span>결제방법:</span>
                <select value={paymentType} onChange={(e) => setPaymentType(e.target.value)}>
                  <option value="kakao">kakao</option>
                  <option value="현금">현금</option>
                  <option value="card">카드</option>
                </select>
                {" || "}
                <span>주문처:</span>
                <select value={orderPost} onChange={(e) => setOrderPost(e.target.value)}>
                  <option value="상계지점">상계지점</option>
                  <option value="중계지점">중계지점</option>
                  <option value="하계지점">하계지점</option>
                </select>
                {" || "}
                <span>주문방법:</span>
                <select value={orderMethod} onChange={(e) => setOrderMethod(e.target.value)}>
                  <option value="배달">배달</option>
                  <option value="택배">택배</option>
                  <option value="직접">직접</option>
                </select>
                {" || "}
                <span>배달주소:</span>
                <input
                  type="text"
                  value={orderAddr}
                  onChange={(e) => setOrderAddr(e.target.value)}
                />
                {" || "}
                <span>총 결제금액:</span> {totalPrice.toLocaleString()}원
              </td>
            </tr>
            <tr>
              <td colSpan="10">
                <button onClick={handlePayment}>결제</button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default PaymentPage;
