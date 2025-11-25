import React, { useEffect, useState, useMemo } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import {
  getCartByToken,
  addItemToCart,
  removeCartItem,
  getCartItemsByPage,
  updateCartItemQuantity, // 새로 추가된 수량 업데이트 API
} from "../../../apis/cart/cartApi";
import "../../../css/cart/CartPage.css";

// ⭐️ [수정] 이미지 로딩을 위한 백엔드 기본 URL 정의
// ShopDetailContainer와 경로를 통일했습니다. (http://localhost:8088/upload/)
const BASE_IMAGE_URL = "http://localhost:8088/upload/"; 
const NO_IMAGE_URL = "/images/noimage.jpg"; // 이미지 없음 파일 경로 추가

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

  // 체크된 아이템 ID 목록
  const [checkedItems, setCheckedItems] = useState(new Set());

  // 선택된 모든 아이템의 {id, price, quantity}를 저장할 Map (페이지 이동 시 유지)
  const [selectedItemsData, setSelectedItemsData] = useState(new Map());

  // JWT 기반 장바구니 조회
  const fetchCart = async () => {
    setLoading(true);
    try {
      const token = localStorage.getItem("accessToken");
      if (!token) throw new Error("로그인이 필요합니다.");

      const data = await getCartByToken(token); // /api/cart/me 호출
      setCart(data);
    } catch (e) {
      console.error("장바구니 불러오기 실패:", e);
      alert("로그인이 필요합니다.");
      navigate("/auth/login", { state: { from: location, itemToAdd } });
    } finally {
      setLoading(false);
    }
  };

  // 장바구니 아이템 조회 (itemSize 필드는 quantity를 의미함)
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
      const fetchedItems = data.content || [];

      // 페이지 이동/검색 시 체크 목록 초기화 (필요에 따라 주석 처리)
      // setCheckedItems(new Set());
      setItems(fetchedItems);
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
  }, [cart, currentPage, keyword]);

  // 상품 자동 추가
  useEffect(() => {
    if (!cart || !itemToAdd) return;

    const addItem = async () => {
      try {
        // 백엔드에서 itemSize를 quantity로 사용합니다.
        // ShopDetailContainer에서 수량을 넘기지 않았으므로 기본 수량 1로 가정
        const quantity = itemToAdd.quantity || 1; // itemToAdd에 quantity가 있으면 사용, 없으면 1
        await addItemToCart(cart.cartId, itemToAdd.id, quantity);
        fetchItems();
        navigate("/cart", { replace: true });
      } catch (e) {
        console.error("상품 추가 실패:", e);
      }
    };

    addItem();
  }, [cart, itemToAdd, navigate]);

  // ⭐️ 아이템 삭제 핸들러
  const handleRemoveItem = async (cartItemId) => {
    try {
      await removeCartItem(cartItemId);
      // 삭제 후 체크 목록에서도 제거
      const newCheckedItems = new Set(checkedItems);
      newCheckedItems.delete(cartItemId);
      setCheckedItems(newCheckedItems);

      setSelectedItemsData((prevMap) => {
        const newMap = new Map(prevMap);
        newMap.delete(cartItemId);
        return newMap;
      });

      // 목록 다시 불러오기
      fetchItems();
    } catch (e) {
      console.error("삭제 실패:", e);
    }
  };

  const handleSearch = (e) => {
    e.preventDefault();
    setCurrentPage(0);
    // fetchItems는 keyword가 변경되면 useEffect에 의해 자동으로 호출됨
  };

  const handlePrev = () =>
    pageInfo.hasPrevious && setCurrentPage((prev) => prev - 1);
  const handleNext = () =>
    pageInfo.hasNext && setCurrentPage((prev) => prev + 1);

  // ⭐️ 체크박스 핸들러: 개별 아이템 체크/해제
  const handleCheckItem = (cartItemId, isChecked) => {
    const newCheckedItems = new Set(checkedItems);
    const newSelectedItemsData = new Map(selectedItemsData);

    const itemData = items.find((item) => item.cartItemId === cartItemId);

    //현재 페이지에서 해당 아이템 정보 찾기
    if (isChecked) {
      newCheckedItems.add(cartItemId);
      //선택시 Map에 가격/수량 데이터 추가
      if (itemData) {
        newSelectedItemsData.set(cartItemId, {
          price: itemData.itemPrice,
          quantity: itemData.itemSize || 1,
        });
      }
    } else {
      newCheckedItems.delete(cartItemId);
      //해제 시 Map에서 데이터 제거
      newSelectedItemsData.delete(cartItemId);
    }
    setCheckedItems(newCheckedItems);
    setSelectedItemsData(newSelectedItemsData);
  };

  // ⭐️ 전체 선택 핸들러
  const handleCheckAll = (isChecked) => {
    const newSelectedItemsData = new Map(selectedItemsData);
    const updatedCheckedItems = new Set(checkedItems);

    if (isChecked) {
      items.forEach((item) => {
        updatedCheckedItems.add(item.cartItemId);
        newSelectedItemsData.set(item.cartItemId, {
          price: item.itemPrice,
          quantity: item.itemSize || 1,
        });
      });
    } else {
      items.forEach((item) => {
        updatedCheckedItems.delete(item.cartItemId);
        newSelectedItemsData.delete(item.cartItemId);
      });
    }
    setCheckedItems(updatedCheckedItems);
    setSelectedItemsData(newSelectedItemsData);
  };

  // ⭐️ 수량 변경 핸들러
  const handleQuantityChange = async (cartItemId, newQuantity) => {
    const quantity = Math.max(1, newQuantity); // 수량은 최소 1

    if (checkedItems.has(cartItemId)) {
      setSelectedItemsData((prevMap) => {
        const newMap = new Map(prevMap);
        if (newMap.has(cartItemId)) {
          newMap.set(cartItemId, {
            ...newMap.get(cartItemId),
            quantity: quantity,
          });
        }
        return newMap;
      });
    }

    // 1. 프론트엔드 상태를 먼저 업데이트 (옵티미스틱 업데이트)
    setItems((prevItems) =>
      prevItems.map((item) =>
        // 백엔드 필드 itemSize를 사용
        item.cartItemId === cartItemId ? { ...item, itemSize: quantity } : item
      )
    );

    // 2. 백엔드 API 연동
    try {
      await updateCartItemQuantity(cartItemId, quantity);
    } catch (e) {
      console.error("수량 업데이트 실패:", e);
      // 실패 시 롤백을 위해 다시 아이템 목록을 불러옵니다.
      fetchItems();
    }
  };

  // ⭐️ 계산된 값 (useMemo 사용)
  const { isAllChecked, checkedItemsTotal } = useMemo(() => {
    const allItemIds = items.map((item) => item.cartItemId);
    // 현재 페이지 아이템 수가 0이 아니면서, 현재 페이지 모든 아이템이 체크되어 있는지 확인
    const isAllChecked =
      items.length > 0 && allItemIds.every((id) => checkedItems.has(id));

    let total = 0;
    for (const data of selectedItemsData.values()) {
      total += (data.price || 0) * (data.quantity || 1);
    }
    return { isAllChecked, checkedItemsTotal: total };
  }, [items, checkedItems, selectedItemsData]);

  if (loading) return <p>로딩 중...</p>;

  // 개별 상품 합계 계산 헬퍼 함수
  const getItemTotal = (item) => {
    const quantity = item.itemSize || 1;
    const price = item.itemPrice || 0;
    return price * quantity;
  };

  return (
    <div className="cartPage">
      <h1>장바구니 목록</h1>

      <form onSubmit={handleSearch} className="cartSearch">
        <input
          type="text"
          placeholder="상품명 검색"
          value={keyword}
          onChange={(e) => setKeyword(e.target.value)}
        />
        <button type="submit">검색</button>
      </form>

      {/* --- 장바구니 테이블 --- */}
      {items.length ? (
        <>
          <table className="cartTable">
            <thead>
              <tr>
                {/* ⭐️ 전체 선택 체크박스 */}
                <th>
                  <input
                    type="checkbox"
                    checked={isAllChecked}
                    onChange={(e) => handleCheckAll(e.target.checked)}
                  />
                </th>
                <th>이미지</th>
                <th>상품명</th>
                <th>가격</th>
                <th>수량</th>
                <th>합계</th>
                <th>삭제</th>
              </tr>
            </thead>
            <tbody>
              {items.map((item) => {
                // ⭐️ [적용] itemImage 필드(파일명)를 사용하여 이미지 URL 생성
                const imageUrl = item.itemImage 
                    ? `${BASE_IMAGE_URL}${item.itemImage}` 
                    : null;

                return (
                  <tr key={item.cartItemId}>
                    {/* ⭐️ 개별 아이템 체크박스 */}
                    <td>
                      <input
                        type="checkbox"
                        checked={checkedItems.has(item.cartItemId)}
                        onChange={(e) =>
                          handleCheckItem(item.cartItemId, e.target.checked)
                        }
                      />
                    </td>

                    {/* ⭐️ [수정] 상품 이미지 열: itemImage를 사용하여 렌더링 */}
                    <td>
                      {imageUrl ? (
                        <img
                          src={imageUrl}
                          alt={item.itemTitle || "상품 이미지"}
                          className="cart-item-thumbnail"
                        />
                      ) : (
                         // 이미지가 없을 때 NO_IMAGE_URL 사용
                        <img
                            src={NO_IMAGE_URL} 
                            alt="이미지 없음"
                            className="cart-item-thumbnail no-image"
                        />
                      )}
                    </td>

                    <td>{item.itemTitle || "상품명 없음"}</td>
                    {/* 가격: itemPrice만 표시 */}
                    <td className="price-column">
                      <span>{item.itemPrice.toLocaleString()}원</span>
                    </td>
                    {/* ⭐️ 수량 변경 UI */}
                    <td>
                      <div className="quantity-control">
                        <button
                          onClick={() =>
                            handleQuantityChange(
                              item.cartItemId,
                              (item.itemSize || 1) - 1
                            )
                          }
                          disabled={(item.itemSize || 1) <= 1}
                        >
                          -
                        </button>
                        <input
                          type="number"
                          min="1"
                          value={item.itemSize || 1}
                          onChange={(e) =>
                            handleQuantityChange(
                              item.cartItemId,
                              parseInt(e.target.value)
                            )
                          }
                        />
                        <button
                          onClick={() =>
                            handleQuantityChange(
                              item.cartItemId,
                              (item.itemSize || 1) + 1
                            )
                          }
                        >
                          +
                        </button>
                      </div>
                    </td>
                    {/* ⭐️ 합계 */}
                    <td>{getItemTotal(item).toLocaleString()}원</td>
                    <td>
                      <button onClick={() => handleRemoveItem(item.cartItemId)}>
                        삭제
                      </button>
                    </td>
                  </tr>
                );
              })}
            </tbody>
          </table>

          {/* ⭐️ 총 금액 표시 영역 */}
          <div className="cartSummary">
            <p>총 상품 가격: {checkedItemsTotal.toLocaleString()}원</p>
            <p>배송비: 무료</p>
            <p className="finalTotal">
              최종 결제 금액: {checkedItemsTotal.toLocaleString()}원
            </p>
          </div>
        </>
      ) : (
        <p>장바구니에 상품이 없습니다.</p>
      )}

      {/* --- 페이지네이션 --- */}
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

      {/* --- 하단 링크 --- */}
      <div className="bottomLinks">
        <button onClick={() => navigate("/store")}>상품 리스트</button>
        <button
          onClick={() =>
            navigate("/payment", {
              state: { checkedItems: Array.from(checkedItems) },
            })
          }
          // 체크된 상품이 있을 때만 결제 버튼 활성화
          disabled={checkedItems.size === 0}
        >
          결제 ({checkedItems.size}개)
        </button>
      </div>
    </div>
  );
}