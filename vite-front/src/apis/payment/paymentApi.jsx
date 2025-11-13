import axios from "axios";

const PAYMENT_API = "http://localhost:8088/api/payments";
const PAYMENT_ITEM_API = "http://localhost:8088/api/payment-items";

export const createPayment = async (paymentData) => {
  const res = await axios.post(PAYMENT_API, paymentData);
  return res.data;
};

export const getAllPayments = async () => {
  const res = await axios.get(PAYMENT_API);
  return res.data;
};

export const deletePayment = async (paymentId) => {
  await axios.delete(`${PAYMENT_API}/${paymentId}`);
};

export const getPaymentItems = async (paymentId) => {
  const res = await axios.get(`${PAYMENT_ITEM_API}/by-payment/${paymentId}`);
  return res.data;
};
