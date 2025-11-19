import axios from 'axios';
import React, { useEffect, useState, useRef } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { loginFn } from '../../../apis/auth/login';
import { useDispatch } from 'react-redux';
import { login } from '../../../slices/loginSlice';
import { setCookie } from '../../../apis/util/cookieUtil';
import { setAccessToken } from '../../../slices/jwtSlice';
import { BACK_BASIC_URL } from '../../../apis/commonApis';

import { initializeThreeScene } from '../../../js/loginThree';
import "../../../css/auth/auth_login.css"

const AuthLoginContainer = () => {

  // 로그인 상태 & 사용자 정보
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");


  const mountRef = useRef(null);
  const [isLoading, setIsLoading] = useState(true);


  const navigate = useNavigate();
  const dispatch = useDispatch();

  // 입력한 값 감지 후 변경
  const onUsernameChange = (e) => {
    setUsername(e.target.value);
  }

  const onPasswordChange = (e) => {
    setPassword(e.target.value);
  }

  const onLoginFn = async () => {
    const rs = await loginFn(username, password);

    // 응답코드
    console.log(rs);

    if (rs.status === 200) {
      const id = rs.data.id;
      const userEmail = rs.data.userEmail;
      const role = rs.data.role;
      const nickName = rs.data.nickName;
      const access = rs.data.accessToken;

      localStorage.setItem("accessToken", access);

      dispatch(login({ userEmail, id, role, nickName, isLogin: true }));
      dispatch(setAccessToken(access));

      navigate("/store/index");
    };
  }

  useEffect(() => {
    const container = mountRef.current;
    if (!container) return;

    let cleanupFunction;

    try {
      cleanupFunction = initializeThreeScene(container);
      // if success init 
      setIsLoading(false);

    } catch (error) {
      console.error("Three.js init ERROR", error);
      setIsLoading(false);
    }

    return () => {
      if (cleanupFunction) {
        cleanupFunction();
      }
    };

  }, []);

  return (

    <div className="login">
      <div ref={mountRef} className='canvas'>
        {isLoading && (
          <div className="3dloading">
            <span className='span-3d'> 3D Loading... </span>
          </div>
        )}
      </div>
      <div className="login-con">
        <h1>로그인</h1>
        <ul>
          <li>
            <input type="text"
              name='username'
              placeholder='email'
              value={username}
              onChange={onUsernameChange} />
          </li>
          <li>
            <input type="password"
              name='password'
              placeholder='password'
              value={password}
              onChange={onPasswordChange} />
          </li>
          <li>
            <button onClick={onLoginFn}>로그인</button>
          </li>
          <div className="login-bottom">
            <li>
              <a href="http://localhost:8088/oauth2/authorization/google">
                <img src="/images/OAuth2/google.png" alt="google" />
              </a>
            </li>
            <li>
              <a href="http://localhost:8088/oauth2/authorization/naver">
                <img src="/images/OAuth2/naver.png" alt="naver" />
              </a>
            </li>
            <li>
              <a href="http://localhost:8088/oauth2/authorization/kakao">
                <img src="/images/OAuth2/kakao.webp" alt="kakao" />
              </a>
            </li>
          </div>
          <li>
            <Link to="/auth/join">회원가입</Link>
            <Link to="/">HOME</Link>
          </li>
        </ul>
      </div>
    </div>

  )
}

export default AuthLoginContainer