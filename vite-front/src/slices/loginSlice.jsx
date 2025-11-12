import { createSlice } from '@reduxjs/toolkit'
import React from 'react'

const initState = {
  id: "",
  userEmail: "",
  isLogin: false
}

const loginSlice = createSlice({
  name: 'LoginSlice',
  initialState: initState,
  reducers: {
    login: (state, action) => {
      console.log("login....");
      const data = action.payload;

      console.log(data);
      return { 
        ...state,
        id: data.id,
        userEmail: data.userEmail,
        isLogin: data.isLogin
      };
    },
    logout: (state, action) => {
      console.log("logout...");
      state.isLogin = false;
      state.userEmail = "";
      state.id = "";
    }
  }
})

export const { login, logout } = loginSlice.actions
export default loginSlice.reducer