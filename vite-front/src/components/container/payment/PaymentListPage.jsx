import React, { useEffect, useState } from "react";
import { getPaymentsByPage } from "../../../apis/payment/paymentApi";
import "../../../css/payment/PaymentPage.css";

const PaymentListPage = () => {
  const [payments, setPayments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [currentPage, setCurrentPage] = useState(0);
  const [pageInfo, setPageInfo] = useState({});
  const [keyword, setKeyword] = useState("");
  const pageSize = 8;

  const fetchPayments = async (page = currentPage) => {
    setLoading(true);
    try {
      const data = await getPaymentsByPage(page, pageSize, keyword);
      console.log("결제 조회 데이터:", data); // 서버 응답 확인용
      setPayments(data.content || []); // content에 배열이 들어있어야 함
      setPageInfo({
        totalPages: data.totalPages || 1,
        hasNext: data.hasNext || false,
        hasPrevious: data.hasPrevious || false,
      });
    } catch (e) {
      console.error("결제 내역 조회 실패:", e);
      setPayments([]);
      setPageInfo({});
    } finally {
      setLoading(false);
    }
  };

  // 페이지 변경이나 초기 로딩 시 호출
  useEffect(() => {
    fetchPayments();
  }, [currentPage]);

  // 검색 제출
  const handleSearch = (e) => {
    e.preventDefault();
    setCurrentPage(0); // 검색 시 첫 페이지로 이동
    fetchPayments(0);
  };

  const handlePrev = () =>
    pageInfo.hasPrevious && setCurrentPage((prev) => prev - 1);
  const handleNext = () =>
    pageInfo.hasNext && setCurrentPage((prev) => prev + 1);

  if (loading) return <p>로딩 중...</p>;
  if (!payments.length) return <p>결제 내역이 없습니다.</p>;

  return (
    <div className="paymentListPage">
      <h1>주문목록</h1>

      {/* 검색 */}
      <form className="paymentSearch" onSubmit={handleSearch}>
        <input
          type="text"
          placeholder="결제 방법 또는 주문처 검색"
          value={keyword}
          onChange={(e) => setKeyword(e.target.value)}
        />
        <button type="submit">검색</button>
      </form>

      {/* 결제 목록 - 그리드 */}
      <div className="paymentGrid">
        {payments.map((payment) => {
          const items = payment.paymentItemEntities || [];
          const totalAmount = items.reduce(
            (sum, item) =>
              sum + (item.paymentItemPrice || 0) * (item.paymentItemSize || 0),
            0
          );

          return (
            <div className="orderCard" key={payment.paymentId}>
              <div className="top">
                <ul>
                  <li>ID: {payment.paymentId}</li>
                  <li>결제방법: {payment.paymentType}</li>
                  <li>주문처: {payment.orderPost}</li>
                  <li>배송주소: {payment.orderAddr}</li>
                  <li>배달방식: {payment.orderMethod}</li>
                  <li>주문금액: {totalAmount.toLocaleString()}원</li>
                </ul>
              </div>
              <div className="bottom">
                <ul>
                  {items.map((item) => (
                    <li key={item.paymentItemId}>
                      <span>{item.paymentItemTitle || "-"}</span>
                      <span>
                        {(item.paymentItemPrice || 0).toLocaleString()}원
                      </span>
                      <span>{item.paymentItemSize || 0}개</span>
                    </li>
                  ))}
                </ul>
              </div>
            </div>
          );
        })}
      </div>

      {/* 페이징 */}
      <div className="pagination">
        <button onClick={handlePrev} disabled={!pageInfo.hasPrevious}>
          이전
        </button>
        <span>
          페이지 {currentPage + 1} / {pageInfo.totalPages || 1}
        </span>
        <button onClick={handleNext} disabled={!pageInfo.hasNext}>
          다음
        </button>
      </div>
    </div>
  );
};

export default PaymentListPage;
