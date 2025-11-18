import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import jwtAxios from "../../../apis/util/jwtUtil";
import { useSelector } from "react-redux";
import { BACK_BASIC_URL } from "../../../apis/commonApis";

import "../../../css/admin/container/AdminItemDetailContainer.css";

const AdminItemDetailContainer = () => {
  const { itemId } = useParams();
  const navigate = useNavigate();
  const accessToken = useSelector((state) => state.jwtSlice.accessToken);

  const [item, setItem] = useState({
    itemTitle: "",
    itemPrice: "",
    itemDetail: "",
    itemSize: "",
    itemImage: "", // 🔥 기존 이미지 URL 받기
  });

  const [file, setFile] = useState(null);

  // 상세 불러오기
  const fetchItemDetail = async () => {
    try {
      const res = await jwtAxios.get(`${BACK_BASIC_URL}/api/admin/item/detail/${itemId}`, {
        headers: { Authorization: `Bearer ${accessToken}` },
      });
      setItem(res.data);
    } catch (err) {
      console.error(err);
      alert("상품 정보를 불러올 수 없습니다.");
    }
  };

  useEffect(() => {
    fetchItemDetail();
  }, []);

  // 수정 처리
  const handleUpdate = async (e) => {
    e.preventDefault();

    const formData = new FormData();
    formData.append("dto", new Blob([JSON.stringify(item)], { type: "application/json" }));

    if (file) {
      formData.append("itemFile", file);
    }

    try {
      await jwtAxios.put(
        `${BACK_BASIC_URL}/api/admin/item/update/${itemId}`,
        formData,
        {
          headers: {
            Authorization: `Bearer ${accessToken}`,
            "Content-Type": "multipart/form-data",
          },
        }
      );

      alert("상품 수정 완료!");
      navigate("/admin/itemList");
    } catch (err) {
      console.error(err);
      alert("상품 수정 실패");
    }
  };

  console.log("TOKEN", accessToken);


  return (
    <div className="admin-item-detail">
      <h2>상품 상세 / 수정</h2>

      {/* 🔥 기존 이미지 미리보기 */}
      {item.itemImage && (
        <img src={item.itemImage} alt="상품 이미지" width="150" />
      )}

      <form onSubmit={handleUpdate}>

        <label>상품명</label>
        <input
          type="text"
          value={item.itemTitle}
          onChange={(e) => setItem({ ...item, itemTitle: e.target.value })}
        />

        <label>가격</label>
        <input
          type="number"
          value={item.itemPrice}
          onChange={(e) => setItem({ ...item, itemPrice: e.target.value })}
        />

        <label>상세 설명</label>
        <textarea
          value={item.itemDetail}
          onChange={(e) => setItem({ ...item, itemDetail: e.target.value })}
        />

        <label>재고</label>
        <input
          type="number"
          value={item.itemSize}
          onChange={(e) => setItem({ ...item, itemSize: e.target.value })}
        />

        <label>상품 이미지 변경</label>
        <input type="file" onChange={(e) => setFile(e.target.files[0])} />

        <button type="submit">수정하기</button>
      </form>
    </div>
  );
};

export default AdminItemDetailContainer;
