import React, { useEffect, useState } from "react";
import jwtAxios from "../../../../apis/util/jwtUtil";
import { useNavigate } from "react-router-dom";
import { BACK_BASIC_URL } from "../../../../apis/commonApis";

import "../../../../css/admin/container/AdminPaymentListContainer.css";
import { useSelector } from "react-redux";

const AdminPaymentListContainer = () => {
  const [payments, setPayments] = useState([]);
  const navigate = useNavigate();
  const accessToken = useSelector((state) => state.jwtSlice.accessToken);

  const fetchData = async () => {
    const res = await jwtAxios.get(`${BACK_BASIC_URL}/api/admin/payments`, {
      headers: { Authorization: `Bearer ${accessToken}` },
    });
    setPayments(res.data.content);
  };

  useEffect(() => {
    fetchData();
  }, []);

  return (
    <div className="admin-payment-list">
      <h2>결제리스트</h2>
      <table>
        <thead>
          <tr>
            <th>결제ID</th>
            <th>회원ID</th>
            <th>금액</th>
            <th>주소</th>
            <th>결제방식</th>
            <th>결제결과</th>
            <th>결제시간</th>
            <th>결제상세보기</th>
          </tr>
        </thead>
        <tbody>
          {payments.map((pay) => (
            <tr key={pay.id}>
              <td>{pay.paymentId}</td>
              <td>{pay.memberId}</td>
              <td>{pay.productPrice.toLocaleString()}원</td>
              <td>{pay.paymentAddr}</td>
              <td>{pay.paymentMethod}</td>
              <td>{pay.paymentResult}</td>
              <td>{pay.createTime}</td>
              <td>
                <button
                  onClick={() =>
                    navigate(`/admin/paymentDetail/${pay.paymentId}`)
                  }
                  className="view-btn"
                >
                  보기
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default AdminPaymentListContainer;
