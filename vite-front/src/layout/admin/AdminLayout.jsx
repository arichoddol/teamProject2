import React, { useEffect, useState } from "react";
import { Outlet, useNavigate } from "react-router-dom";
import AdminHeader from "./AdminHeader";
import AdminSidebar from "./AdminSidebar";
import Footer from "../../components/common/Footer"; // 기존 footer 재사용

import "../../css/admin/AdminLayout.css";
import "../../css/admin/AdminHeader.css";
import "../../css/admin/AdminSidebar.css";
import "../../css/common/footer.css";
import { useSelector } from "react-redux";
import jwtAxios from "../../apis/util/jwtUtil";
import { BACK_BASIC_URL } from "../../apis/commonApis";

const AdminLayout = () => {
  const accessToken = useSelector((state) => state.jwtSlice.accessToken);
  const role = useSelector((state) => state.loginSlice.role);
  const navigate = useNavigate();
  const [hasPendingApproval, setHasPendingApproval] = useState(false);

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
      console.log(pendingRs);
      setHasPendingApproval(pendingRs);
    } catch (err) {
      console.log("크루 신청 목록 불러오기 실패 " + err);
    }
  };

  useEffect(() => {
    if (role !== "ADMIN") {
      console.log("접근 권한이 없습니다.");
      navigate("/");
      return;
    }
  }, [role]);

  useEffect(() => {
    crewReqeustFn();
  }, []);

  return (
    <div className="admin-layout">
      <AdminHeader />

      <div className="admin-container">
        <div className="admin-container-left">
          <AdminSidebar hasPendingApproval={hasPendingApproval} />
        </div>

        <main className="admin-container-right">
          <Outlet />
        </main>
      </div>

      <Footer />
    </div>
  );
};

export default AdminLayout;
