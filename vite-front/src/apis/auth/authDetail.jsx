import axios from "axios";
import { BACK_BASIC_URL } from "../commonApis";
import store from "../../store/store";
import jwtAxios from "../util/jwtUtil";

export const authDetailFn = async () => {
  const accessToken = store.getState().jwtSlice.accessToken;
  const memberId = store.getState().loginSlice.id;

  try {
    const res = await jwtAxios.get(
      `${BACK_BASIC_URL}/api/member/detail/${memberId}`,
      {
        headers: { Authorization: `Bearer ${accessToken}` },
        withCredentials: true,
      }
    );

    return res;
  } catch (err) {
    console.log("회원 조회를 실패했습니다.");
  }
};

// app.jsx 새로고침 일어날때 재로그인 성공 후 memberDetail을 뽑아옵니다.
export const indexUserDetailFn = async (token, id) => {
  try {
    const res = await jwtAxios.get(
      `${BACK_BASIC_URL}/api/member/detail/${id}`,
      {
        headers: { Authorization: `Bearer ${token}` },
        withCredentials: true,
      }
    );

    return res;
  } catch (err) {
    console.log("회원 조회를 실패했습니다.");
  }
};
