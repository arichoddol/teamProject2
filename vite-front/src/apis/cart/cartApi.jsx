import jwtAxios from "../util/jwtUtil";

const CART_API = "http://localhost:8088/api/cart";

// === 장바구니 조회 ===
// memberId는 JWT에서 추출됨 → 프론트에서 넣지 않음
export const getCartByMemberId = async () => {
  const res = await jwtAxios.get(`${CART_API}`, {
    withCredentials: true,
  });

  return res.data;
};

// === 장바구니 생성 ===
export const createCart = async () => {
  const res = await jwtAxios.post(`${CART_API}`, {}, {
    withCredentials: true,
  });
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
