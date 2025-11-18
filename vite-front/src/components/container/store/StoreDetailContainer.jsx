import axios from "axios";
import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router";

import "../../../css/store/storeDetail.css";

const ShopDetailContainer = () => {
  const [item, setItem] = useState({});
  const [content, setContent] = useState("");
  const [replies, setReplies] = useState([]);
  const { id } = useParams();
    const navigate = useNavigate(); // navigate 추가


  const API_BASE_URL = "http://localhost:8088/api/shop";

  const fetchData = async () => {
    const response = await axios.get(`${API_BASE_URL}/detail/${id}`);

    if (response.data) {
      setItem(response.data);
    } else {
      console.log("게시물 데이터가 존재하지 않음.");
    }
  };

  useEffect(() => {
    fetchData();
    // when id change its always restart it.
  }, [id]);

   // 장바구니 담기 함수
  const handleAddToCart = (product) => {
    // CartPage로 이동 + state에 상품 정보 전달
    navigate("/cart", { state: { itemToAdd: product } });
  };

  return (
    <div className="itemDetail">
      <div className="itemDetail-con">
        <h4>{item.itemTitle}</h4>
        <div className="itemDetail-con-image"></div>
        <div className="itemDetail-con-info">
          <span>상품ID : {item.id}</span>
          <br />
          <span>상세설명 : {item.itemDetail}</span>
          <br />
          <span>상품가격 : {item.itemPrice}</span>
          <br />
          <span>itemSize(temp) : {item.itemSize}</span>
          <br />
          <span>attachFile(temp) : {item.attachFile}</span>
          <br />
          <span>createTime : {item.createTime}</span>
          <br />
          <span>updateTime : {item.updatTime}</span>
          <br />
          <button onClick={() => handleAddToCart(item)}>장바구니 담기</button>
        </div>
      </div>
    </div>
  );
};

export default ShopDetailContainer;
