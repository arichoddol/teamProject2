import axios from "axios";
import { BACK_BASIC_URL } from "../commonApis";

const ACCESS_TOKEN_KEY = localStorage.getItem("accessToken");

export const authMyPaymentFn = async (memberId) => {
  try {
    const res = await axios(
      `${BACK_BASIC_URL}/api/payments/myPayment/${memberId}`
    );
    return res;
  } catch (err) {
    console.log("회원 결제페이지 조회 실패, ", err);
  }
};
