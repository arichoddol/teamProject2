import { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import { authMyPaymentFn } from "../../../apis/auth/authPayment";
import { formatDate } from "../../../js/formatDate";

const AuthPaymentContainer = () => {
  const paymentStatus = {
    PENDING: "배송중",
    COMPLETED: "배송완료",
    FAILED: "결제실패",
    CANCELED: "주문취소",
    REFUNDED: "환불완료",
  };

  const [myPayment, setMyPayment] = useState([]);
  const memberId = useSelector((state) => state.loginSlice.id);

  const myPagePaymentFn = async () => {
    const res = await authMyPaymentFn(memberId);
    setMyPayment(res.data.content);
  };

  useEffect(() => {
    myPagePaymentFn();
  }, []);
  return (
    <div className="auth-my-payment">
      <div className="auth-my-payment-search">
        <h1>결제목록</h1>
        <div className="auth-my-payment-search-con">
          <input
            type="text"
            name=""
            id=""
            placeholder="주문한 상품을 검색할 수 있어요."
          />
          <button>검색</button>
        </div>
      </div>
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
                      <span>박한나</span>
                      <span>010-0000-0000</span>
                      <span>서울특별시 노원구</span>
                    </div>
                  </div>

                  <div className="my-payment-type">
                    <h4>결제정보</h4>
                    <div className="my-payment-type-detail">
                      <div className="my-payment-type-detail-top">
                        <h5>주문금액</h5>
                        <span>{el.productPrice}원</span>
                      </div>
                      <div className="my-payment-type-detail-bottom">
                        <h5>결제방법</h5>
                        <span>{el.paymentType}</span>
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
                            <span>{items.price}원</span>
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
      </div>
    </div>
  );
};

export default AuthPaymentContainer;
