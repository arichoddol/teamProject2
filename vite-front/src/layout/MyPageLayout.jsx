import React, { useState } from "react";
import Header from "../components/common/Header";
import { Outlet } from "react-router";
import Footer from "../components/common/Footer";

const MyPageLayout = () => {
  const [activeTab, setActiveTap] = useState("회원정보");

  return (
    <>
      <Header />
      <div className="memberDetail">
        <h1>myPage</h1>
        <div className="memberDetail-con">
          <aside className="memberDetail-left">
            <nav>
              <ul>
                <li
                  className={activeTab === "회원정보" ? "active" : ""}
                  onClick={() => setActiveTap("회원정보")}
                >
                  회원정보
                </li>
                <li
                  className={activeTab === "결제정보" ? "active" : ""}
                  onClick={() => setActiveTap("결제정보")}
                >
                  결제정보
                </li>
              </ul>
            </nav>
          </aside>
          <Outlet />
        </div>
      </div>
      <Footer />
    </>
  );
};

export default MyPageLayout;
