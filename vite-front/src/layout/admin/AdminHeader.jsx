import React from "react";
import { Link } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import { logoutAction } from "../../slices/loginSlice";
import { deleteAccessToken } from "../../slices/jwtSlice";
import { removeCookie } from "../../apis/util/cookieUtil";
import LogoutBtn from "../../apis/auth/LogoutBtn";

const AdminHeader = () => {
  const dispatch = useDispatch();
  const userEmail = useSelector((state) => state.loginSlice.userEmail);

  return (
    <header className="admin-header">
      <div className="admin-header-left">
        <Link to="/admin/index" className="logo">
          ADMIN
        </Link>
      </div>

      <div className="admin-header-right">
        <ul>
          <li>{userEmail}</li>
          <li>
            <Link to="/">SHOP</Link>
          </li>
          <li>
            <LogoutBtn />
          </li>
        </ul>
      </div>
    </header>
  );
};

export default AdminHeader;
