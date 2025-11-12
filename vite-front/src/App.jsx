
import './App.css'
import { RouterProvider } from 'react-router-dom'
import root from './router/root';
import { useEffect, useState } from 'react';
import axios from 'axios';

function App() {

  

  return (
    <>
      {/* <ScrollTopBtn/> */}
      <RouterProvider router={root}/>
    </>
  )
}

export default App;
