import React from "react";
import { Outlet } from "react-router-dom";
import AdminHeader from "./AdminHeader";
import AdminSidebar from "./AdminSidebar";
import Footer from "../../components/common/Footer";  // 기존 footer 재사용

import "../../css/admin/AdminLayout.css";
import "../../css/admin/AdminHeader.css";
import "../../css/admin/AdminSidebar.css";
import "../../css/common/footer.css";




const AdminLayout = () => {
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
