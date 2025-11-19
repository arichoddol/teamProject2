
import './App.css'
import { RouterProvider } from 'react-router-dom'
import root from './router/root';
import { useEffect } from 'react';
import { useDispatch } from 'react-redux';
import { login } from './slices/loginSlice';
import { setAccessToken } from './slices/jwtSlice';

function App() {

  const dispatch = useDispatch();

  useEffect(() => {
    const token = localStorage.getItem("accessToken");
    
    if (token) {
      const payload = JSON.parse(atob(token.split('.')[1])); 
      dispatch(login({
        id: payload.id || "",
        userEmail: payload.userEmail || "",
        role: payload.role || "",
        nickName: payload.nickName || "",
        isLogin: true
      }));
    }

    dispatch(setAccessToken(token));
  }, []);

  return (
    <>
      {/* <ScrollTopBtn/> */}
      <RouterProvider router={root}/>
    </>
  )
}

export default App;
