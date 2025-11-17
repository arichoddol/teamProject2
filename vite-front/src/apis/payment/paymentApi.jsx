import axios from "axios";

const PAYMENT_API = "http://localhost:8088/api/payments";
const PAYMENT_ITEM_API = "http://localhost:8088/api/payment-items";

// JWT 가져오기
const getToken = () => localStorage.getItem("token");

// -----------------------------
// CRUD API
// -----------------------------
export const createPayment = async (paymentData) => {
  const res = await axios.post(PAYMENT_API, paymentData, {
    headers: { Authorization: `Bearer ${getToken()}` },
  });
  return res.data;
};

export const getAllPayments = async () => {
  const res = await axios.get(PAYMENT_API, {
    headers: { Authorization: `Bearer ${getToken()}` },
  });
  return res.data;
};

export const deletePayment = async (paymentId) => {
  await axios.delete(`${PAYMENT_API}/${paymentId}`, {
    headers: { Authorization: `Bearer ${getToken()}` },
  });
};

export const getPaymentItems = async (paymentId) => {
  const res = await axios.get(`${PAYMENT_ITEM_API}/by-payment/${paymentId}`, {
    headers: { Authorization: `Bearer ${getToken()}` },
  });
  return res.data;
};

// -----------------------------
// KakaoPay 연동
// -----------------------------
export const pgRequest = async (pg, productId, memberId, productPrice, productName) => {
  if (pg !== "kakao") throw new Error("지원되지 않는 PG사입니다.");
  const res = await axios.get(`${PAYMENT_API}/pg/${pg}`, {
    params: { productId, memberId, productPrice, productName },
    headers: { Authorization: `Bearer ${getToken()}` },
  });
  return res.data.approvalUrl; // 카카오 결제 페이지 URL 반환
};

export const approvalPayment = async (paymentId, productPrice, productName, memberId, pgToken) => {
  await axios.get(
    `${PAYMENT_API}/approval/${paymentId}/${productPrice}/${productName}/${memberId}`,
    {
      params: { pg_token: pgToken },
      headers: { Authorization: `Bearer ${getToken()}` },
    }
  );
};
