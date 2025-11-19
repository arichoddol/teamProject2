import React, { useEffect, useState } from "react";
import { getAllPayments } from "../../../apis/payment/paymentApi";
import "../../../css/payment/PaymentPage.css";

const PaymentListPage = () => {
  const [payments, setPayments] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchPayments = async () => {
      try {
        const data = await getAllPayments();
        setPayments(data);
      } catch (e) {
        console.error("결제 내역 불러오기 실패:", e);
      } finally {
        setLoading(false);
      }
    };
    fetchPayments();
  }, []);

  if (loading) return <p>로딩 중...</p>;
  if (!payments.length) return <p>결제 내역이 없습니다.</p>;

  return (
    <div className="paymentList">
      <h1>주문목록</h1>
      {payments.map((payment) => {
        const items = payment.paymentItemEntities || [];
        const totalAmount = items.reduce(
          (sum, item) => sum + (item.paymentItemPrice || 0) * (item.paymentItemSize || 0),
          0
        );

        return (
          <div className="order" key={payment.paymentId}>
            <div className="top">
              <ul>
                <li>ID: {payment.paymentId}</li>
                <li>결제방법: {payment.paymentType}</li>
                <li>주문처: {payment.orderPost}</li>
                <li>배송주소: {payment.orderAddr}</li>
                <li>배달방식: {payment.orderMethod}</li>
                <li>주문금액: {totalAmount.toLocaleString()}원</li>
              </ul>
            </div>
            <div className="bottom">
              <ul>
                {items.map((item) => (
                  <li key={item.paymentItemId}>
                    <span>{item.paymentItemTitle || "-"}</span>
                    <span>{(item.paymentItemPrice || 0).toLocaleString()}원</span>
                    <span>{item.paymentItemSize || 0}개</span>
                  </li>
                ))}
              </ul>
            </div>
          </div>
        );
      })}
    </div>
  );
};

export default PaymentListPage;
