import axios from 'axios';
import React from 'react'
import { BACK_BASIC_URL } from '../commonApis';
import { setAccessToken } from '../../slices/jwtSlice';
import { login } from '../../slices/loginSlice';
import store from '../../store/store';

// 로그인 요청 보내는 함수입니다.
export const loginFn = async (username, password) => {
  
  console.log(username, password);
  const form = new FormData();

  form.append('username', username);
  form.append('password', password);

  // header 요청 -> form으로
  const header = { headers : { "Content-Type" : "application/x-www-form-urlencoded" } };

  let rs;

  try {
    // axios는 기본이 json이므로 다른 형태를 보낼땐 header 정보를 넣어준다.
    rs = await axios.post(`${BACK_BASIC_URL}/api/member/login`, form, header);
    if (rs.status == 200) {
      alert("로그인 성공!");
      console.log(rs);      
    }
  } catch(err) {
    if (err.status == 401) {
      console.log(err.status);
      alert("로그인 실패!");
      return;
    }
  }

  // 로그인 정보 넘기기 (응답코드)
  return rs;
}
