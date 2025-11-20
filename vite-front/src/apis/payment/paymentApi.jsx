import jwtAxios from "../util/jwtUtil"; // JWT 자동 갱신 처리된 axios

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
// 페이징 + 검색 API
// -----------------------------
export const getPaymentsByPage = async (page = 0, size = 5, keyword = "") => {
  const res = await jwtAxios.get(`${PAYMENT_API}/page`, {
    params: {
      page,
      size,
      keyword
    }
  });
  return res.data;
};

// -----------------------------
// KakaoPay 연동
// -----------------------------

// ⭐️ [수정된 부분] GET에서 POST로 변경, 단일 상품 파라미터 대신 paymentDto 객체 하나를 받음
export const pgRequest = async (pg, paymentDto) => {
  if (pg !== "kakao") throw new Error("지원되지 않는 PG사입니다.");

  // ⭐️ [415 해결] POST 요청 시, 세 번째 인자에 Content-Type 헤더를 명시
  const res = await jwtAxios.post(`${PAYMENT_API}/pg/${pg}`, paymentDto, {
      headers: {
          'Content-Type': 'application/json' 
      }
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