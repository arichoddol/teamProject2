import jwtAxios from "../util/jwtUtil";

const CART_API = "http://localhost:8088/api/cart";

// JWT에서 memberId 추출 함수
const getMemberIdFromJWT = () => {
  const memberStr = document.cookie
    .split("; ")
    .find(row => row.startsWith("member="))?.split("=")[1];

  if (!memberStr) return null;

  const member = JSON.parse(decodeURIComponent(memberStr));
  return member.id;
};

// === 장바구니 조회 ===
export const getCartByMemberId = async () => {
  const memberId = getMemberIdFromJWT();
  if (!memberId) throw new Error("로그인이 필요합니다.");

  const res = await jwtAxios.get(`${CART_API}/${memberId}`, {
    withCredentials: true,
  });
  return res.data;
};

// === 장바구니 생성 ===
export const createCart = async () => {
  const memberId = getMemberIdFromJWT();
  if (!memberId) throw new Error("로그인이 필요합니다.");

  const res = await jwtAxios.post(
    `${CART_API}/${memberId}`,
    {},
    { withCredentials: true }
  );
  return res.data;
};

// === 장바구니 아이템 추가 ===
export const addItemToCart = async (cartId, itemId, itemSize = 1) => {
  const res = await jwtAxios.post(
    `${CART_API}/${cartId}/item`,
    {},
    {
      withCredentials: true,
      params: { itemId, itemSize },
    }
  );
  return res.data;
};

// === 장바구니 아이템 삭제 ===
export const removeCartItem = async (cartItemId) => {
  await jwtAxios.delete(`${CART_API}/item/${cartItemId}`, {
    withCredentials: true,
  });
};
