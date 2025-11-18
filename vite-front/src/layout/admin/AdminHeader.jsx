import React from 'react';
import { Link } from 'react-router-dom';
import { useDispatch } from 'react-redux';
import { logout } from '../../slices/loginSlice';
import { deleteAccessToken } from '../../slices/jwtSlice';
import { removeCookie } from '../../apis/util/cookieUtil';

const AdminHeader = () => {

  const dispatch = useDispatch();

  const onAdminLogout = () => {
    removeCookie("member");
    removeCookie("refreshToken");
    dispatch(logout());
    dispatch(deleteAccessToken());
    alert("관리자 로그아웃 완료");
    window.location.href = "/auth/login";
  }

  return (
    <header className="admin-header">
      <div className="admin-header-left">
        <Link to="/admin/index" className="logo">ADMIN</Link>
      </div>

      <div className="admin-header-right">
        <span className="admin-info">관리자 페이지</span>
        <button onClick={onAdminLogout} className="logout-btn">로그아웃</button>
      </div>
    </header>
  );
};

export default AdminHeader;
