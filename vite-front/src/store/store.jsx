import React from 'react'
import { configureStore } from '@reduxjs/toolkit'
import loginSlice from '../slices/loginSlice'
import jwtSlice from '../slices/jwtSlice'
import CartSlice from '../slices/CartSlice'


const store = configureStore({
  reducer : { 
    "loginSlice" : loginSlice,
    "jwtSlice" : jwtSlice,
    "cartSlice" : CartSlice
  }
})

export default store;
