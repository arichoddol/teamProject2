import store from "../../store/store";
import { BACK_BASIC_URL } from "../commonApis";
import jwtAxios from "../util/jwtUtill";

export const adminFn = async () => {
  const accessToken = store.getState().jwtSlice.accessToken;
  
  try {
    const res = await jwtAxios.get(`${BACK_BASIC_URL}/api/admin/`,
      { headers: { Authorization: `Bearer ${accessToken}` },
        withCredentials: true,}
      );
    
    return res;

  } catch(err) {
    console.log("어드민 접근 실패했습니다.");
  }
}