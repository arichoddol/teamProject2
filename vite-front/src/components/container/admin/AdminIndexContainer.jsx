import React, { useEffect, useState } from 'react';
import { useSelector } from 'react-redux';
import { adminFn } from '../../../apis/admin/adminIndex';
import { useNavigate } from 'react-router-dom';

import "../../../css/admin/container/AdminIndexContainer.css"
import { formattedPrice } from '../../../js/formatDate';


const AdminIndexContainer = () => {
  const navigate = useNavigate();
  const accessToken = useSelector((state) => state.jwtSlice.accessToken);

  const [summary, setSummary] = useState({
    totalMembers: 0,
    todayMembers: 0,
    totalCrews: 0,
    todayCrews: 0,
    totalPayments: 0,
    todayPayments: 0,
    totalSales:0,
    todaySales:0,
    totalBoards: 0
  });

  // 회원 관련
  const [member, setMember] = useState({
    totalMembers: 0,
    todayMembers: 0
  });

  // 크루 관련
  const [crew, setCrew] = useState({
    totalCrews: 0,
    todayCrews: 0
  });

  // 결제 관련
  const [payment, setPayment] = useState({
    totalPayments: 0,
    todayPayments: 0,
    totalSales: 0,
    todaySales: 0
  });

  // 게시판 관련
  const [board, setBoard] = useState({
    totalBoards: 0,
    todayBoards: 0
  });

  // 필요하면 상품 관련도 -> 신상 상품 있는지 없는지도 있으면 좋으니

  // 추가로 필요할 수 있는 것들
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  // (핵심) 대시보드 요약 데이터 불러오기
  const fetchSummary = async () => {
    try {
      setLoading(true);

      const res = await adminFn.getSummary(accessToken);
      setSummary(res.data);  // 데이터 구조에 따라 수정 필요

      setMember({
        totalMembers: res.data.totalMembers,
        todayMembers: res.data.todayMembers
      })

      setCrew({
        totalCrews: res.data.totalCrews,
        todayCrews: res.data.todayCrews
      })

      setPayment({
        totalPayments: res.data.totalPayments,
        todayPayments: res.data.todayPayments,
        totalSales: res.data.totalSales,
        todaySales: res.data.todaySales
      })

      setBoard({
        totalBoards: res.data.totalBoards,
        todayBoards: res.data.todayBoards
      })


    } catch (err) {
      console.log("관리자 요약 조회 실패:", err);
      setError("데이터를 가져올 수 없습니다.");

      // 토큰 문제일 경우 처리
      // navigate("/login"); 같은 로직 추가 가능
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (!accessToken) {
      navigate("/login");
      return;
    }

    fetchSummary();
  }, [accessToken]);

  return (
    <div>
      {/* 로딩 */}
      {loading && <p>로딩 중...</p>}

      {/* 에러 */}
      {error && <p style={{ color: "red" }}>{error}</p>}

      {/* 데이터 출력 (예시) */}
      <div className="indexCon">
        <div className="summaryCard">
          <h3>SIMPLE SUMMARY</h3>
          <ul>
            <li>총 회원수: {summary.totalMembers}</li>
            <li>오늘 가입자수: {summary.todayMembers}</li>
            <li>총 크루수: {summary.totalCrews}</li>
            <li>오늘 생성크루수: {summary.todayCrews}</li>
            <li>총 결제건: {summary.totalPayments}</li>
            <li>오늘 결제건: {summary.todayPayments}</li>
            <li>총 게시글 수: {summary.totalBoards}</li>
          </ul>
        </div>
        <div className="memberCard">
          <h3>MEMBER</h3>
          <ul>
            <li>총 회원: {member.totalMembers}</li>
            <li>오늘 가입한 회원: {member.todayMembers}</li>
          </ul>
        </div>
        <div className="crewCard">
          <h3>CREW</h3>
          <ul>
            <li>총 크루: {crew.totalCrews}</li>
            <li>오늘 가입한 크루: {crew.todayCrews}</li>
          </ul>
        </div>
        <div className="paymentCard">
          <h3>PAYMENT</h3>
          <ul>
            <li>총 결제건: {payment.totalPayments}</li>
            <li>오늘 결제건: {payment.todayPayments}</li>
            <li>총 매출액: {formattedPrice(payment.totalSales)} 원 </li>
            <li>일간 매출액: {formattedPrice(payment.todaySales)} 원</li>
          </ul>
        </div>
        <div className="boardCard">
          <h3>BOARD</h3>
          <ul>
            <li>총 게시물: {board.totalBoards}</li>
            <li>오늘 게시물: {board.todayBoards}</li>
          </ul>
        </div>
      </div>
    </div>
  );
};

export default AdminIndexContainer;
