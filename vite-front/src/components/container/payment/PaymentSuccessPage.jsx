import React from "react";
import { useNavigate } from "react-router-dom";
import "../../../css/payment/PaymentPage.css";

const PaymentSuccessPage = () => {
  const navigate = useNavigate();

  const goToPaymentList = () => {
    navigate("/payment/list"); // PaymentListPage 라우트 경로에 맞게 수정
  };

  return (
    <div className="paymentSuccess">
      <h2>🎉 결제 성공!</h2>
      <p>결제가 정상적으로 완료되었습니다.</p>
      <button className="btnPaymentList" onClick={goToPaymentList}>
        결제 내역 확인
      </button>
    </div>
  );
};

export default PaymentSuccessPage;
