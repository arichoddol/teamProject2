import axios from "axios";

// 백엔드 API 주소
const CART_API = "http://localhost:8088/api/cart";

export const getCartByMemberId = async (memberId) => {
  const res = await axios.get(`${CART_API}/${memberId}`);
  return res.data;
};

export const createCart = async (memberId) => {
  const res = await axios.post(`${CART_API}/${memberId}`);
  return res.data;
};

export const addItemToCart = async (cartId, itemId, itemSize) => {
  const res = await axios.post(`${CART_API}/${cartId}/item`, null, {
    params: { itemId, itemSize },
  });
  return res.data;
};

export const removeCartItem = async (cartItemId) => {
  await axios.delete(`${CART_API}/item/${cartItemId}`);
};
