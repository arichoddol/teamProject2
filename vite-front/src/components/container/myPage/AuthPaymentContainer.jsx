import { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import { authMyPaymentFn } from "../../../apis/auth/authPayment";
import { formatDate, formattedPrice } from "../../../js/formatDate";
import { useNavigate } from "react-router";
import AdminPagingComponent from "../../common/AdminPagingComponent";

const AuthPaymentContainer = () => {
  const paymentStatus = {
    PENDING: "배송중",
    COMPLETED: "배송완료",
    FAILED: "결제실패",
    CANCELED: "주문취소",
    REFUNDED: "환불완료",
  };

  const paymentType = {
    CARD: "카드결제",
    CASH: "현금결제",
    KAKAO: "카카오페이",
  };

  const [myPayment, setMyPayment] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);
  const [search, setSearch] = useState("");
  const [defaultSearch, setDefaultSearch] = useState("");
  const [pageData, setPageData] = useState({});
  const memberId = useSelector((state) => state.loginSlice.id);
  const navigate = useNavigate();

  const myPagePaymentFn = async () => {
    const res = await authMyPaymentFn(memberId, currentPage, search);
    setMyPayment(res.data.content);
    setPageData(res.data);
    console.log(res);
  };

  const hadlePageChange = (page) => {
    setCurrentPage(page);
  };

  useEffect(() => {
    myPagePaymentFn();
  }, [currentPage, defaultSearch]);
  return (
    <div className="auth-my-payment">
      <div className="auth-my-payment-search">
        <h1>결제목록</h1>
        <div className="auth-my-payment-search-con">
          <input
            type="text"
            name="keyowrd"
            id="keyowrd"
            placeholder="주문한 상품을 검색할 수 있어요."
            onChange={(e) => {
              setSearch(e.target.value);
            }}
            onKeyDown={(e) => {
              if (e.key == "Enter") {
                setCurrentPage(1);
                setDefaultSearch(e.target.value);
              }
            }}
          />
          <button
            onClick={() => {
              setCurrentPage(1);
              setDefaultSearch(search);
            }}
          >
            검색
          </button>
        </div>
      </div>
      {myPayment.length <= 0 ? (
        defaultSearch != "" ? (
          <div className="my-payment-no-show">
            <div className="my-payment-no-show-con">
              <div className="my-payment-no-show-title">
                <img src="/images/myPage/boxEmpty.png" alt="" />
                <h1>'{defaultSearch}'에 해당하는 주문 내역이 없습니다. 😭</h1>
                <span>다른 키워드로 다시 검색해 보세요.</span>
                <button
                  onClick={() => {
                    navigate("/store");
                  }}
                >
                  스토어 이동
                </button>
              </div>
            </div>
          </div>
        ) : (
          <div className="my-payment-no-show">
            <div className="my-payment-no-show-con">
              <div className="my-payment-no-show-title">
                <img src="/images/myPage/boxEmpty.png" alt="빈상자" />
                <h1>💳 주문 내역이 없습니다.</h1>
                <span>
                  첫 주문을 기다리고 있습니다! 지금 바로 쇼핑을 시작해 보세요.
                </span>
                <button
                  onClick={() => {
                    navigate("/store");
                  }}
                >
                  구매하러가기
                </button>
              </div>
            </div>
          </div>
        )
      ) : (
        <div className="auth-my-payment-con">
          {myPayment.map((el) => {
            return (
              <div className="my-payment-detail-all" key={el.paymentId}>
                <div className="my-payment-status-con">
                  <h2>{formatDate(el.createTime)}</h2>
                  <div className="my-payment-status-detail">
                    <span
                      style={{
                        color:
                          el.paymentStatus === "PENDING"
                            ? "#fc9a1bff"
                            : el.paymentStatus === "COMPLETED"
                            ? "#3b82f6"
                            : el.paymentStatus === "FAILED"
                            ? "red"
                            : el.paymentStatus === "CANCELED"
                            ? "#fc9a1bff"
                            : el.paymentStatus === "REFUNDED"
                            ? "green"
                            : "block",
                      }}
                    >
                      {paymentStatus[el.paymentStatus] || el.paymentStatus}
                    </span>
                  </div>
                </div>

                <div className="my-payment-content">
                  <div className="my-payment-left">
                    <div className="my-payment-address">
                      <h4>배송지</h4>
                      <div className="my-payment-address-detail">
                        <span>{el.paymentReceiver}</span>
                        <span>{el.paymentPhone}</span>
                        <span>{el.paymentAddr}</span>
                      </div>
                    </div>

                    <div className="my-payment-type">
                      <h4>결제정보</h4>
                      <div className="my-payment-type-detail">
                        <div className="my-payment-type-detail-top">
                          <h5>주문금액</h5>
                          <span>{formattedPrice(el.productPrice)}원</span>
                        </div>
                        <div className="my-payment-type-detail-bottom">
                          <h5>결제방법</h5>
                          <span>
                            {paymentType[el.paymentType] || el.paymentType}
                          </span>
                        </div>
                      </div>
                    </div>
                  </div>

                  <div className="my-payment-right">
                    <h4>주문상품</h4>
                    <div className="my-payment-items-wrapper">
                      {el.paymentItems.map((items, idx) => {
                        return (
                          <div className="my-payment-item" key={idx}>
                            <div className="my-payment-item-title">
                              {items.s3file ? (
                                <div className="my-payment-item-img">
                                  <img src={items.s3file} alt="" />
                                </div>
                              ) : (
                                <div className="my-payment-item-img">
                                  <img
                                    src="https://dummyimage.com/150x150/cccccc/000000&text=No+Image"
                                    alt="상품 이미지"
                                  />
                                </div>
                              )}
                              <h4>{items.title}</h4>
                              <span>{items.size}개</span>
                              <span>{formattedPrice(items.price)}원</span>
                            </div>
                          </div>
                        );
                      })}
                    </div>
                  </div>
                </div>
              </div>
            );
          })}
          <AdminPagingComponent
            pageData={pageData}
            onPageChange={hadlePageChange}
          />
        </div>
      )}
    </div>
  );
};

export default AuthPaymentContainer;
