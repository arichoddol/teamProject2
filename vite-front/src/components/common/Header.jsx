import React from 'react'
import { useDispatch, useSelector } from 'react-redux';
import { logoutFn } from '../../apis/auth/logout';
import { removeCookie } from '../../apis/util/cookieUtil';
import { logout } from '../../slices/loginSlice';
import { deleteAccessToken } from '../../slices/jwtSlice';
import { Link } from 'react-router-dom';
// CSS 
import "../../css/common/header.css"

// slice 테스트 확인용으로 작성했습니다.
const Header = () => {

  const dispatch = useDispatch();
  const isLogin = useSelector(state => state.loginSlice.isLogin);
  const role = useSelector(state => state.loginSlice.role);

  const onLogoutFn = async () => {
    const rs = await logoutFn();

    if (rs == 200) {
      removeCookie("member");
      removeCookie("refreshToken");
      dispatch(logout());
      dispatch(deleteAccessToken());

      alert("로그아웃 성공!");
    }
  }

  return (

    <div className="header">
      <div className="nav">
        <h1>HOME</h1>
        <div className="gnb">
          <ul>
       {isLogin ? (
              // **로그인 상태일 때 메뉴**
              <>
                <li>
                  <button onClick={onLogoutFn}>LOGOUT</button>
                </li>
                <li>
                  <Link to="/auth/myPage">myPage</Link>
                </li>
                <li>
                  <Link to="/mycrew/1">myCrew</Link>
                </li>
                {isAdmin && 
                  <li>
                    <Link to="/admin/index">ADMIN</Link>
                  </li>
                }
              </>
             ) : (
              <>
              <li>
                <Link to= "/mycrew/1">myCrew</Link>
              </li>
              <li>
              <Link to="/auth/login">LOGIN</Link>
              </li> 
              { role === 'ADMIN' ? <li><Link to= "/admin/index">ADMIN</Link></li> : null }
              </>
            )}
            <li>
              <Link to="/board">BOARD</Link>
            </li>
            <li>
              <Link to="/store">STORE</Link>
            </li>
            <li>
              <Link to="/crew">CREW</Link>
            </li>
            <li>
              <Link to={"/event"}>EVENT</Link>
            </li>
          </ul>
        </div>
      </div>
    </div>

  )
}

export default Header