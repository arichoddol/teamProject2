import axios from "axios";
import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

const ShopMainContainer = () => {
  const [items, setItems] = useState([]);
  const navigate = useNavigate();

  // 상품 리스트 가져오기
  const fetchData = async () => {
    try {
      const response = await axios.get("http://localhost:8088/api/shop");
      if (response.data && response.data.content) {
        setItems(response.data.content);
      }
    } catch (e) {
      console.error("상품 리스트 불러오기 실패:", e);
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  // 장바구니 담기 버튼 클릭
  const handleAddToCart = (item) => {
    navigate("/cart", { state: { itemToAdd: item } }); 
    // memberId 없이 이동, 서버에서 JWT로 확인
  };

  return (
    <div className="itemList">
      <h2>상품 리스트</h2>
      <table className="item-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>상품명</th>
            <th>가격</th>
            <th>장바구니</th>
          </tr>
        </thead>
        <tbody>
          {items.map((list) => (
            <tr key={list.id}>
              <td>{list.id}</td>
              <td>{list.itemTitle}</td>
              <td>{list.itemPrice.toLocaleString()}원</td>
              <td>
                <button onClick={() => handleAddToCart(list)}>장바구니 담기</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default ShopMainContainer;
