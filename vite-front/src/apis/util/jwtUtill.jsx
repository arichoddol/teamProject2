import axios from 'axios'
import { BACK_BASIC_URL } from '../commonApis'
import { setCookie } from './cookieUtil';
import store from '../../store/store';
import { setAccessToken } from '../../slices/jwtSlice';

const jwtAxios = axios.create();

// ----------- 리프레시 토큰을 불러와서 액세스 토큰 갱신 ----------- 
const refreshTokenFn = async () => {
  const res = await axios.post(`${BACK_BASIC_URL}/api/refresh/token`, {}, { withCredentials: true });
  const accessToken = res.data.accessToken;
  store.dispatch(setAccessToken(accessToken));
  return res;
}

// ----------- before request 요청 인터셉터 -----------
const beforeReq = (config) => {
  console.log("before request....");
  const accessToken = store.getState().jwtSlice.accessToken;
  // const memberInfo = getCookie("member");
  // header에 access 토큰이 없으면 로그인 안되어있는걸로 간주
  if(!accessToken) {
    console.log("Member Not Found");
    return Promise.reject({
        response:
        { data: { error: "REQUIRE_LOGIN" } }
      });
  }
  return config;
}

// ----------- fail request 요청 실패 인터셉터 -----------
const requestFail = (err) => {
  console.log("Request error...");
  return Promise.reject(err);
}

// ----------- before return response 응답 인터셉터 -----------
const beforeRes = async (res) => {
  console.log("before return response....");
  return res;
}

// ----------- response fail -----------
const responseFail = async (err) => {
  console.log("response fail error....(access 토큰 갱신 필요)");
  
  if (err.status == 401) {
    let rs;

    try {
      rs = await refreshTokenFn();
      const id = store.getState().loginSlice.id;
      const userEmail = store.getState().loginSlice.userEmail;
      const isLogin = store.getState().loginSlice.isLogin;
  
      const memberData = {
          id: id,
          status: isLogin,
          userEmail: userEmail
          }

      const memberValue = JSON.stringify(memberData);
      // 만료정보 재발급
      setCookie("member", memberValue, 1); 

    } catch(err) {
      console.log("리프레시 토큰 갱신 실패 ", + err);
      return Promise.reject(err);
    }

    const originalRequest = err.config;
    originalRequest.headers.Authorization = `Bearer ${rs.data.accessToken}`;

    return await jwtAxios(originalRequest);
  }

  return Promise.reject(err);
}

// 요청 인터셉터(beforeReq): 요청을 보내기 전에 엑세스 토큰을 Authorization 헤더에 추가
jwtAxios.interceptors.request.use(beforeReq, requestFail);

// 응답 인터셉터(beforeReq): 응답이 오면 엑세스 토큰이 만료되었는지 확인하고, 만료된 경우 자동으로 토큰을 갱신하여 원래의 요청을 재시도
jwtAxios.interceptors.response.use(beforeRes, responseFail);

export default jwtAxios;