import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { NavLink, useLocation } from "react-router-dom";
import { BACK_BASIC_URL } from "../../apis/commonApis";
import jwtAxios from "../../apis/util/jwtUtil";
import { hasRequestStatus } from "../../slices/adminSlice";

const AdminSidebar = () => {
  const location = useLocation();
  const accessToken = useSelector((state) => state.jwtSlice.accessToken);
  const rqStatus = useSelector((state) => state.adminSlice.requestStatus);
  const dispatch = useDispatch();

  const crewReqeustFn = async () => {
    try {
      const res = await jwtAxios.get(
        `${BACK_BASIC_URL}/api/admin/crew/create/requestList`,
        {
          headers: { Authorization: `Bearer ${accessToken}` },
          withCredentials: true,
        }
      );

      const crewRequestData = res.data.content;
      const pendingRs = crewRequestData.some((el) =>
        el.status.includes("PENDING")
      );
      dispatch(hasRequestStatus(pendingRs));
      console.log(rqStatus);
    } catch (err) {
      console.log("크루 신청 목록 불러오기 실패 " + err);
    }
  };

  useEffect(() => {
    crewReqeustFn();
  }, [location]);

  return (
    <aside className="admin-sidebar">
      <ul>
        <li>
          <NavLink to="/admin/index">📊 대시보드</NavLink>
        </li>

        <li>
          <NavLink to="/admin/memberList">👤 멤버 관리</NavLink>
        </li>

        <li>
          <NavLink to="/admin/crewList">👥 크루 관리</NavLink>
        </li>
        <li className="hasPendingApproval">
          <NavLink to="/admin/crewAllow">
            📝 크루개설 승인
            {rqStatus === true ? <span /> : null}
          </NavLink>
        </li>

        <li>
          <NavLink to="/admin/boardList">🗂 커뮤니티 관리</NavLink>
        </li>

        <li>
          <NavLink to="/admin/eventList">🎉 이벤트목록 관리</NavLink>
        </li>
        <li>
          <NavLink to="/admin/addEvent">➕ 이벤트 등록</NavLink>
        </li>

        <li>
          <NavLink to="/admin/itemList">🛒 상품목록 관리</NavLink>
        </li>
        <li>
          <NavLink to="/admin/addItem">➕ 상품등록</NavLink>
        </li>

        <li>
          <NavLink to="/admin/paymentList">💳 결제목록 관리</NavLink>
        </li>
      </ul>
    </aside>
  );
};

export default AdminSidebar;
