import React from 'react'
import { configureStore } from '@reduxjs/toolkit'
import loginSlice from '../slices/loginSlice'
import jwtSlice from '../slices/jwtSlice'

const store = configureStore({
  reducer : { 
    "loginSlice" : loginSlice,
    "jwtSlice" : jwtSlice
  }
})

export default store;
