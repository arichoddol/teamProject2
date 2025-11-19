import React from "react";
import { NavLink } from "react-router-dom";

const AdminSidebar = () => {
  return (
    <aside className="admin-sidebar">
      <ul>

        <li><NavLink to="/admin/index">📊 대시보드</NavLink></li>

        <li><NavLink to="/admin/memberList">👤 멤버 관리</NavLink></li>

        <li><NavLink to="/admin/crewList">👥 크루 관리</NavLink></li>
        <li><NavLink to="/admin/crewAllow">📝 크루개설 승인</NavLink></li>

        <li><NavLink to="/admin/boardList">🗂 커뮤니티 관리</NavLink></li>

        <li><NavLink to="/admin/eventList">🎉 이벤트목록 관리</NavLink></li>
        <li><NavLink to="/admin/addEvent">➕ 이벤트 등록</NavLink></li>

        <li><NavLink to="/admin/itemList">🛒 상품목록 관리</NavLink></li>
        <li><NavLink to="/admin/addItem">➕ 상품등록</NavLink></li>

        <li><NavLink to="/admin/paymentList">💳 결제목록 관리</NavLink></li>

      </ul>
    </aside>
  );
};

export default AdminSidebar;
