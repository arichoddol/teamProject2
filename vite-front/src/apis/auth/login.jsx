import axios from "axios";
import React from "react";
import { BACK_BASIC_URL } from "../commonApis";

export const loginFn = async (username, password) => {
  console.log(username, password);
  const form = new FormData();

  form.append("username", username);
  form.append("password", password);

  // header 요청 -> form으로
  const header = {
    headers: { "Content-Type": "application/x-www-form-urlencoded" },
  };

  let rs;

  try {
    rs = await axios.post(`${BACK_BASIC_URL}/api/member/login`, form, header);
    if (rs.status == 200) {
      alert("로그인 성공!");
    }
  } catch (err) {
    if (err.status == 401) {
      console.log(err.status);
      alert("로그인 실패!");
      return;
    }
  }
  return rs;
};
