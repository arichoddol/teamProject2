import React, { useEffect, useState } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import {
  getCartByMemberId,
  createCart,
  addItemToCart,
  removeCartItem,
  getCartItemsByPage,
} from "../../../apis/cart/cartApi";
import "../../../css/cart/CartPage.css";

export default function CartPage() {
  const navigate = useNavigate();
  const location = useLocation();
  const itemToAdd = location.state?.itemToAdd;

  const [cart, setCart] = useState(null);
  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(true);
  const [currentPage, setCurrentPage] = useState(0);
  const [pageInfo, setPageInfo] = useState({});
  const [keyword, setKeyword] = useState("");
  const pageSize = 5;

  // 장바구니 조회 또는 생성
  const fetchCart = async () => {
    setLoading(true);
    try {
      let data = await getCartByMemberId();
      if (!data) data = await createCart();
      setCart(data);
    } catch (e) {
      console.error("장바구니 불러오기 실패:", e);
      alert("로그인이 필요합니다.");
      navigate("/auth/login", { state: { from: location, itemToAdd } });
    } finally {
      setLoading(false);
    }
  };

  // 장바구니 아이템 조회 (페이징 + 검색)
  const fetchItems = async () => {
    if (!cart) return;
    setLoading(true);
    try {
      const data = await getCartItemsByPage(
        cart.cartId,
        currentPage,
        pageSize,
        keyword
      );
      setItems(data.content || []);
      setPageInfo({
        totalPages: data.totalPages,
        hasNext: data.hasNext,
        hasPrevious: data.hasPrevious,
      });
    } catch (e) {
      console.error("장바구니 아이템 조회 실패:", e);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchCart();
  }, []);

  useEffect(() => {
    fetchItems();
  }, [cart, currentPage]);

  // 전달된 상품 자동 추가
  useEffect(() => {
    if (!cart || !itemToAdd) return;

    const addItem = async () => {
      try {
        const newItem = await addItemToCart(cart.cartId, itemToAdd.id, 1);
        setItems((prev) => [...prev, newItem]);
        navigate("/cart", { replace: true }); // state 초기화
      } catch (e) {
        console.error("상품 추가 실패:", e);
      }
    };

    addItem();
  }, [cart, itemToAdd, navigate]);

  const handleRemoveItem = async (cartItemId) => {
    try {
      await removeCartItem(cartItemId);
      setItems((prev) => prev.filter((item) => item.cartItemId !== cartItemId));
    } catch (e) {
      console.error("삭제 실패:", e);
    }
  };

  const handleSearch = (e) => {
    e.preventDefault();
    setCurrentPage(0); // 검색 시 페이지 초기화
    fetchItems();
  };

  const handlePrev = () =>
    pageInfo.hasPrevious && setCurrentPage((prev) => prev - 1);
  const handleNext = () =>
    pageInfo.hasNext && setCurrentPage((prev) => prev + 1);

  if (loading) return <p>로딩 중...</p>;
  if (!items.length) return <p>장바구니에 상품이 없습니다.</p>;

  return (
    <div className="cartPage">
      <h1>장바구니 목록</h1>

      {/* 검색 폼 */}
      <form
        onSubmit={(e) => {
          e.preventDefault();
          fetchItems(); // Enter 누르거나 버튼 클릭할 때만 호출
          setCurrentPage(0); // 검색 시 페이지 초기화
        }}
        className="cartSearch"
      >
        <input
          type="text"
          placeholder="상품명 검색"
          value={keyword}
          onChange={(e) => setKeyword(e.target.value)} // 단순 state 업데이트
        />
        <button type="submit">검색</button>
      </form>

      {/* 장바구니 테이블 */}
      <table className="cartTable">
        <thead>
          <tr>
            <th>ID</th>
            <th>상품명</th>
            <th>가격</th>
            <th>삭제</th>
          </tr>
        </thead>
        <tbody>
          {loading ? (
            <tr>
              <td colSpan={4}>로딩중...</td>
            </tr>
          ) : (
            items.map((item) => (
              <tr key={item.cartItemId}>
                <td>{item.cartItemId}</td>
                <td>{item.itemTitle || "상품명 없음"}</td>
                <td>{item.itemPrice.toLocaleString()}원</td>
                <td>
                  <button onClick={() => handleRemoveItem(item.cartItemId)}>
                    삭제
                  </button>
                </td>
              </tr>
            ))
          )}
        </tbody>
      </table>

      {/* 페이징 */}
      <div className="pagination">
        <button onClick={handlePrev} disabled={!pageInfo.hasPrevious}>
          이전
        </button>
        <span>
          페이지 {currentPage + 1} / {pageInfo.totalPages}
        </span>
        <button onClick={handleNext} disabled={!pageInfo.hasNext}>
          다음
        </button>
      </div>

      <div className="bottomLinks">
        <button onClick={() => navigate("/shop")}>상품 리스트</button>
        <button onClick={() => navigate("/payment")}>결제</button>
      </div>
    </div>
  );
}
