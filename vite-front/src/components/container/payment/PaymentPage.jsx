import React, { useEffect, useState } from "react";
import { useNavigate, useLocation } from "react-router-dom"; 
import { getCartByToken } from "../../../apis/cart/cartApi"; 
import { pgRequest } from "../../../apis/payment/paymentApi"; 
import "../../../css/payment/PaymentPage.css";

// ⭐️ 카트 아이템을 백엔드 PaymentItemDto 형식으로 변환합니다.
const mapCartItemsToPaymentItems = (cartItems) => {
    return cartItems.map(item => ({
        // PaymentItemDto의 필드와 매핑 (itemId, price, size, title은 필수)
        itemId: item.itemId,           // Long
        price: item.itemPrice,         // Integer
        size: item.itemSize,           // Integer
        title: item.itemTitle,         // String
        s3file: item.s3file || null,   // String (Cart item 구조에 따라 유연하게 처리)
        // 백엔드에서 필요한 다른 필드가 있다면 추가해야 합니다.
    }));
};


const PaymentPage = () => {
    const navigate = useNavigate();
    const location = useLocation(); 
    const { checkedItems: itemsToPayIds = [] } = location.state || {}; 

    const [cart, setCart] = useState(null);
    const [loading, setLoading] = useState(true);
    const [paymentType, setPaymentType] = useState("kakao");
    
    // ⚠️ TODO: 실제 배송 정보는 폼에서 받아야 합니다.
    const [address, setAddress] = useState("서울시 강남구"); 
    const [postcode, setPostcode] = useState("06180");
    const [method, setMethod] = useState("배달");


    useEffect(() => {
        const fetchCart = async () => {
            try {
                const data = await getCartByToken();
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

    const handlePayment = async () => {
        const itemsToPay = getItemsToPay(); 

        if (itemsToPay.length === 0) {
            alert("결제할 상품을 선택해주세요.");
            return;
        }

        try {
            const totalPrice = calculateTotalPrice(itemsToPay); 
            const memberId = cart.memberId;
            if (!memberId) throw new Error("회원 정보가 없습니다.");

            // ⭐️ [수정 핵심] 백엔드 PaymentDto 구조에 맞게 데이터 객체 생성
            const paymentDto = {
                memberId: memberId, 
                paymentAddr: address, 
                paymentPost: postcode,
                paymentMethod: method, 
                paymentType: paymentType.toUpperCase(), // KAKAO, CARD 등 대문자 처리
                
                // ⭐️ PaymentItemDto 리스트 매핑
                paymentItems: mapCartItemsToPaymentItems(itemsToPay), 
                
                productPrice: totalPrice, // Long
                isSucceeded: 0 // Integer (초기값 0)
            };
            

            if (paymentType === "kakao") {
                // ⭐️ [수정] pgRequest 함수 호출 시, pg와 paymentDto 객체만 전달
                const approvalUrl = await pgRequest("kakao", paymentDto);
                window.location.href = approvalUrl;
            } else {
                alert("현금/카드 결제는 아직 지원되지 않습니다.");
            }
        } catch (e) {
            console.error("결제 실패:", e);
            alert("결제 실패: " + e.message);
        }
    };

    const itemsToDisplay = getItemsToPay();
    const totalPrice = calculateTotalPrice(itemsToDisplay);

    if (loading) return <p>로딩 중...</p>;
    
    if (!cart || itemsToDisplay.length === 0) return <p>결제할 상품이 없습니다. 장바구니에서 상품을 선택해주세요.</p>;

    return (
        <div className="payment">
            <h1>결제목록 ({itemsToDisplay.length}개)</h1>
            
            {/* ⭐️ 결제 대상 아이템만 렌더링 */}
            {itemsToDisplay.map((item) => (
                <div key={item.cartItemId}>
                    {item.itemTitle} - {item.itemPrice.toLocaleString()}원 x {item.itemSize || 1}개 
                </div>
            ))}
            
            <div>총 결제금액: {totalPrice.toLocaleString()}원</div>

            {/* 임시 배송 정보 입력 필드 */}
            <div>
                <label>주소:</label>
                <input type="text" value={address} onChange={(e) => setAddress(e.target.value)} />
            </div>
            <div>
                <label>우편번호:</label>
                <input type="text" value={postcode} onChange={(e) => setPostcode(e.target.value)} />
            </div>
            
            <div>
                결제방법:
                <select value={paymentType} onChange={(e) => setPaymentType(e.target.value)}>
                    <option value="kakao">Kakao Pay</option>
                    <option value="card">카드</option>
                    <option value="현금">현금</option>
                </select>
            </div>

            <button onClick={handlePayment}>결제하기</button>
        </div>
    );
};

export default PaymentPage;