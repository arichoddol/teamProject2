import React from 'react'
import store from '../../store/store'
import axios from 'axios';
import { BACK_BASIC_URL } from '../commonApis';

// JWT 토큰
export const logoutFn = async () => {
  const ACCESS_TOKEN_KEY = 'accessToken';
  const accessToken = store.getState().jwtSlice.accessToken;

  try {
    const res = await axios.post(`${BACK_BASIC_URL}/api/member/logout`,
      {}, {
        headers: {
          Authorization: `Bearer ${accessToken}`
        },
        withCredentials: true,
      }
    );

    localStorage.removeItem(ACCESS_TOKEN_KEY);

    return res.status;
    
  } catch(err) {
    console.log("로그아웃 처리 중 오류 발생:", + err);
  }
}
