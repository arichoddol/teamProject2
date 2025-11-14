import axios from "axios";

const CART_API = "http://localhost:8088/api/cart";

// 회원 장바구니 조회
export const getCartByMemberId = async (memberId) => {
  const res = await axios.get(`${CART_API}/${memberId}`);
  return res.data;
};

// 회원 장바구니 생성
export const createCart = async (memberId) => {
  const res = await axios.post(`${CART_API}/${memberId}`);
  return res.data;
};

// 장바구니에 아이템 추가
export const addItemToCart = async (cartId, itemId, itemSize = 1) => {
  const res = await axios.post(`${CART_API}/${cartId}/item`, null, {
    params: { itemId, itemSize },
  });
  return res.data;
};

// 장바구니 아이템 삭제
export const removeCartItem = async (cartItemId) => {
  await axios.delete(`${CART_API}/item/${cartItemId}`);
};
