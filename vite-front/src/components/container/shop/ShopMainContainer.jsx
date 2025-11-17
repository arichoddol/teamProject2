import axios from 'axios';
import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

const ShopMainContainer = () => {
  const [items, setItems] = useState([]);
  const navigate = useNavigate();
  const memberId = 1; // 예시: 로그인한 회원 ID

  const fetchData = async () => {
    const response = await axios.get("http://localhost:8088/api/shop");
    if(response.data && response.data.content){
      setItems(response.data.content);
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  const handleAddToCart = (item) => {
    navigate(`/cart/${memberId}`, { state: { itemToAdd: item } });
  };

  return (
    <div className="itemList">
      <h2>상품 리스트</h2>
      <table className='item-table'>
        <thead>
          <tr>
            <th>ID</th>
            <th>상품명</th>
            <th>가격</th>
            <th>장바구니</th>
          </tr>
        </thead>
        <tbody>
          {items.map(list => (
            <tr key={list.id}>
              <td>{list.id}</td>
              <td>{list.itemTitle}</td>
              <td>{list.itemPrice}</td>
              <td>
                <button onClick={() => handleAddToCart(list)}>
                  장바구니 담기
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  )
}

export default ShopMainContainer;
