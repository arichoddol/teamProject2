import jwtAxios from "../util/jwtUtill"; // JWT 자동 갱신 처리된 axios

const PAYMENT_API = "http://localhost:8088/api/payments";
const PAYMENT_ITEM_API = "http://localhost:8088/api/payment-items";

// -----------------------------
// CRUD API
// -----------------------------

export const createPayment = async (paymentData) => {
  const res = await jwtAxios.post(PAYMENT_API, paymentData);
  return res.data;
};

export const getAllPayments = async () => {
  const res = await jwtAxios.get(PAYMENT_API);
  return res.data;
};

export const deletePayment = async (paymentId) => {
  await jwtAxios.delete(`${PAYMENT_API}/${paymentId}`);
};

export const getPaymentItems = async (paymentId) => {
  const res = await jwtAxios.get(`${PAYMENT_ITEM_API}/by-payment/${paymentId}`);
  return res.data;
};

// -----------------------------
// KakaoPay 연동
// -----------------------------

export const pgRequest = async (pg, productId, productPrice, productName, memberId) => {
  if (pg !== "kakao") throw new Error("지원되지 않는 PG사입니다.");

  const res = await jwtAxios.get(`${PAYMENT_API}/pg/${pg}`, {
    params: { 
      productId: Number(productId),      
      productPrice: Number(productPrice), 
      productName: encodeURIComponent(String(productName)), 
      memberId: Number(memberId)
    },
  });

  return res.data.approvalUrl;
};

// -----------------------------
// 결제 승인 (success 페이지에서 호출)
// -----------------------------
export const approvePayment = async (paymentId, productPrice, productName, memberId, pgToken) => {
  if (!pgToken) throw new Error("pg_token이 필요합니다.");

  await jwtAxios.get(
    `${PAYMENT_API}/approval/${paymentId}/${Number(productPrice)}/${encodeURIComponent(String(productName))}/${memberId}`,
    { params: { pg_token: pgToken } }
  );
};
