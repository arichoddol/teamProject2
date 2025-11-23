import React from "react";
import { useDispatch, useSelector } from "react-redux";
import LogoutBtn from "./LogoutBtn";
import HeaderStore from "../common/HeaderModal/HeaderStore";
import { Link } from "react-router-dom";

// CSS
import "../../css/common/header.css";

// slice 테스트 확인용으로 작성했습니다.
const Header = () => {
  const dispatch = useDispatch();
  const isLogin = useSelector((state) => state.loginSlice.isLogin);
  const role = useSelector((state) => state.loginSlice.role);
  const memberId = useSelector((state) => state.loginSlice.id); // 추가

  return (
    <div className="header">
      <div className="nav">
        <h1>HOME</h1>
        <div className="gnb">
          <ul>
            {isLogin ? (
              // **로그인 상태일 때 메뉴**
              <>
                <LogoutBtn />
                <li>
                  <Link to="/cart">CART</Link>
                </li>
                <li>
                  <Link to="/store">SHOP</Link>
                </li>

                <li>
                  <Link to="/myPage">myPage</Link>
                </li>
                {role === "ADMIN" ? (
                  <li>
                    <Link to="/admin/index">ADMIN</Link>
                  </li>
                ) : null}
              </>
            ) : (
              <>
                <li>
                  <Link to="/auth/login">LOGIN</Link>
                </li>
                <li>
                  <Link to="/auth/join">JOIN</Link>
                </li>
              </>
            )}
            <li>
              <Link to="/board">BOARD</Link>
            </li>
            <HeaderStore>
              <li>
                <Link to="/store">STORE</Link>
              </li>
            </HeaderStore>
            {/* <li>
              <Link to="/store">STORE</Link>
            </li> */}
            <li>
              <Link to="/crew/list">CREW</Link>
            </li>
            <li>
              <Link to={"/event"}>EVENT</Link>
            </li>
            <li>
              <Link to="/api/marathon">MARATHON</Link>
            </li>
          </ul>
        </div>
      </div>
    </div>
  );
};

export default Header;
