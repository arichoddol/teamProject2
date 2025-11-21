import React, { useEffect, useState } from "react";
import { Outlet, useLocation, useNavigate } from "react-router-dom";
import AdminHeader from "./AdminHeader";
import AdminSidebar from "./AdminSidebar";
import Footer from "../../components/common/Footer"; // 기존 footer 재사용

import "../../css/admin/AdminLayout.css";
import "../../css/admin/AdminHeader.css";
import "../../css/admin/AdminSidebar.css";
import "../../css/common/footer.css";
import { useDispatch, useSelector } from "react-redux";

const AdminLayout = () => {
  const role = useSelector((state) => state.loginSlice.role);
  const navigate = useNavigate();

  useEffect(() => {
    if (role !== "ADMIN") {
      console.log("접근 권한이 없습니다.");
      navigate("/");
      return;
    }
  }, [role]);

  return (
    <div className="admin-layout">
      <AdminHeader />

      <div className="admin-container">
        <div className="admin-container-left">
          <AdminSidebar />
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
