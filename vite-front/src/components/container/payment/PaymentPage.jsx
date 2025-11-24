import React, { useEffect, useState, useMemo } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import { getCartByToken } from "../../../apis/cart/cartApi";
import { pgRequest } from "../../../apis/payment/paymentApi";
import "../../../css/payment/PaymentPage.css";
// 🚚 배송지 컴포넌트 등 필요 시 추가

// ⭐️ 카트 아이템을 백엔드 PaymentItemDto 형식으로 변환합니다. (s3file 제거)
const mapCartItemsToPaymentItems = (cartItems) => {
    return cartItems.map(item => ({
        // PaymentItemDto의 필드와 매핑 (itemId, price, size, title은 필수)
        itemId: item.itemId,           // Long
        price: item.itemPrice,         // Integer
        size: item.itemSize,           // Integer
        title: item.itemTitle,         // String
        // s3file 필드 제거
    }));
};


const PaymentPage = () => {
    const navigate = useNavigate();
    const location = useLocation();
    // 장바구니 페이지에서 넘어온 결제 대상 cartItemId 리스트
    const { checkedItems: itemsToPayIds = [] } = location.state || {};

    const [cart, setCart] = useState(null);
    const [loading, setLoading] = useState(true);
    // paymentType의 초기값은 소문자입니다. (select option value에 따라)
    const [paymentType, setPaymentType] = useState("kakao"); 

    // ⭐️ [추가] 받는 분 이름, 전화번호
    const [receiverName, setReceiverName] = useState("홍길동"); 
    const [receiverPhone, setReceiverPhone] = useState("010-1234-5678"); 
    
    // 배송지 정보 (임시 데이터)
    const [address, setAddress] = useState("서울시 강남구 테헤란로 123");
    const [postcode, setPostcode] = useState("06180");
    const [method, setMethod] = useState("배송");


    // 장바구니 전체 데이터 조회 (결제 대상 아이템 정보 포함)
    useEffect(() => {
        const fetchCart = async () => {
            try {
                const data = await getCartByToken();
                // 장바구니 데이터를 확인하고 설정
                setCart(data?.items?.length ? data : null);
            } catch (e) {
                console.error("장바구니 불러오기 실패:", e);
                alert("로그인이 필요합니다.");
                navigate("/auth/login");
            } finally {
                setLoading(false);
            }
        };
        fetchCart();
    }, [navigate]);

    // ⭐️ 결제 대상 아이템만 필터링하는 함수
    const getItemsToPay = () => {
        if (!cart || !cart.items) return [];

        if (itemsToPayIds.length > 0) {
            return cart.items.filter(item =>
                itemsToPayIds.includes(item.cartItemId)
            );
        }

        return [];
    };

    // ⭐️ 결제 대상 아이템의 총 금액을 계산하는 함수
    const calculateTotalPrice = (items) => {
        return items.reduce(
            (sum, item) => sum + (item.itemPrice || 0) * (item.itemSize || 1),
            0
        );
    };

    // 결제 핸들러
    const handlePayment = async () => {
        const itemsToPay = getItemsToPay();

        if (itemsToPay.length === 0) {
            alert("결제할 상품을 선택해주세요.");
            return;
        }

        // ⚠️ 필수 배송 정보 확인
        if (!receiverName || !receiverPhone || !address || !postcode) {
            alert("이름, 연락처, 주소, 우편번호를 모두 입력해주세요.");
            return;
        }

        try {
            const totalPrice = calculateTotalPrice(itemsToPay);
            const memberId = cart.memberId;
            if (!memberId) throw new Error("회원 정보가 없습니다.");
            
            // ⭐️ 백엔드 PaymentDto 구조에 맞게 데이터 객체 생성
            const paymentDto = {
                memberId: memberId,
                // ⭐️ [추가] 받는 분 이름, 전화번호
                paymentReceiver: receiverName, 
                paymentPhone: receiverPhone,
                
                paymentAddr: address,
                paymentPost: postcode,
                paymentMethod: method, // 배송 방식
                paymentType: paymentType.toUpperCase(), // KAKAO, CARD, CASH 등 대문자 처리

                // PaymentItemDto 리스트 매핑
                paymentItems: mapCartItemsToPaymentItems(itemsToPay),

                productPrice: totalPrice, // Long
                // 현금/카드 결제 시 '성공' 처리를 위해 isSucceeded를 1로 설정하여 API에 전달
                isSucceeded: paymentType === 'kakao' ? 0 : 1, 
            };
            

            if (paymentType === "kakao") {
                // 카카오페이: PG로 리다이렉트할 URL 받기
                const approvalUrl = await pgRequest("kakao", paymentDto);
                window.location.href = approvalUrl;
            } else {
                // ⭐️ [핵심 수정 부분] "success" 대신 실제 결제 타입("CARD" 또는 "CASH")을 전달합니다.
                // paymentApi.jsx에서 이 타입들을 허용하도록 수정했기 때문에 이제 오류가 나지 않습니다.
                await pgRequest(paymentType.toUpperCase(), paymentDto); 

                alert("결제가 완료되었습니다.");
                // 성공 페이지 또는 결제 내역 페이지로 이동
                navigate("/payment/success", { replace: true });
            }
        } catch (e) {
            console.error("결제 실패:", e);
            alert("결제 실패: " + (e.response?.data || e.message));
        }
    };

    const itemsToDisplay = getItemsToPay();
    const totalPrice = calculateTotalPrice(itemsToDisplay);

    if (loading) return <div className="loading-state">잠시만 기다려주세요...</div>;

    if (!cart || itemsToDisplay.length === 0) return (
        <div className="payment-empty">
            <p>결제할 상품이 없습니다. 장바구니에서 상품을 선택해주세요.</p>
            <button onClick={() => navigate("/cart")}>장바구니로 돌아가기</button>
        </div>
    );

    return (
        <div className="paymentPage">
            <h1>🛍️ 주문/결제</h1>

            <div className="payment-container">
                {/* 1. 결제 대상 목록 */}
                <div className="payment-section payment-items">
                    <h2>📦 주문 상품 ({itemsToDisplay.length}개)</h2>
                    <table className="payment-table">
                        <thead>
                            <tr>
                                <th>상품 정보</th>
                                <th>수량</th>
                                <th>가격</th>
                                <th>합계</th>
                            </tr>
                        </thead>
                        <tbody>
                            {itemsToDisplay.map((item) => (
                                <tr key={item.cartItemId}>
                                    <td className="item-title">
                                        {/* ⚠️ 이미지 필드 삭제 */}
                                        <span>{item.itemTitle}</span>
                                    </td>
                                    <td>{item.itemSize || 1}</td>
                                    <td>{item.itemPrice.toLocaleString()}원</td>
                                    <td className="item-total">
                                        {(item.itemPrice * (item.itemSize || 1)).toLocaleString()}원
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>

                {/* 2. 배송/결제 정보 및 최종 금액 */}
                <div className="payment-info-box">
                    
                    {/* ⭐️ [추가] 받는 분 정보 */}
                    <div className="payment-section receiver-info">
                        <h2>👥 받는 분 정보</h2>
                        <div className="form-group">
                            <label htmlFor="receiver-name">받는 분 이름</label>
                            <input 
                                id="receiver-name"
                                type="text" 
                                value={receiverName} 
                                onChange={(e) => setReceiverName(e.target.value)} 
                            />
                        </div>
                        <div className="form-group">
                            <label htmlFor="receiver-phone">연락처</label>
                            <input 
                                id="receiver-phone"
                                type="text" 
                                value={receiverPhone} 
                                onChange={(e) => setReceiverPhone(e.target.value)} 
                            />
                        </div>
                    </div>
                    
                    {/* 배송지 정보 */}
                    <div className="payment-section shipping-info">
                        <h2>🚚 배송지 정보</h2>
                        <div className="form-group">
                            <label htmlFor="address">받는 주소</label>
                            <input 
                                id="address"
                                type="text" 
                                value={address} 
                                onChange={(e) => setAddress(e.target.value)} 
                            />
                        </div>
                        <div className="form-group">
                            <label htmlFor="postcode">우편번호</label>
                            <input 
                                id="postcode"
                                type="text" 
                                value={postcode} 
                                onChange={(e) => setPostcode(e.target.value)} 
                            />
                        </div>
                    </div>

                    {/* 결제 방법 선택 */}
                    <div className="payment-section payment-method">
                        <h2>💳 결제 수단</h2>
                        <div className="form-group">
                            <label htmlFor="payment-select">결제 방법</label>
                            <select 
                                id="payment-select" 
                                value={paymentType} 
                                onChange={(e) => setPaymentType(e.target.value)}
                            >
                                <option value="kakao">카카오페이 (PG)</option>
                                <option value="CARD">신용/체크카드 (즉시 결제)</option>
                                <option value="CASH">현금 결제 (즉시 결제)</option>
                            </select>
                        </div>
                    </div>

                    {/* 최종 결제 금액 요약 */}
                    <div className="payment-summary">
                        <h3>최종 결제 금액</h3>
                        <div className="summary-row">
                            <span>상품 금액</span>
                            <span>{totalPrice.toLocaleString()}원</span>
                        </div>
                        <div className="summary-row">
                            <span>배송비</span>
                            <span>0원 (무료)</span>
                        </div>
                        <div className="summary-row total-price">
                            <strong>총 결제 금액</strong>
                            <strong>{totalPrice.toLocaleString()}원</strong>
                        </div>

                        {/* 결제 버튼 */}
                        <button 
                            onClick={handlePayment} 
                            className={`payment-button ${paymentType === 'kakao' ? 'kakao-pay-button' : 'default-button'}`}
                        >
                            {paymentType === 'kakao' ? '카카오페이로 결제하기' : '결제하기'}
                        </button>
                    </div>

                </div>
            </div>
        </div>
    );
};

export default PaymentPage;