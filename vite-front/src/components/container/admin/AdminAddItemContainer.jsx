import React, { useState } from 'react'
import axios from "axios";

const AdminAddItemContainer = () => {
  const [itemDto, setItemDto] = useState({
    itemTitle: '',
    itemPrice: '',
    itemSize: '',
    itemDetail: '',

  });

  const [file, setFile] = useState(null)

  const handleSubmit = async (e) => {
    e.preventDefault();

    const formData = new FormData();

    formData.append(
      "itemDto", new Blob([JSON.stringify(itemDto)], { type: "application/json" })
    );

    formData.append("itemFile", file);

    try {

      await axios.post("/api/admin/item/insert", formData, {
        headers: { "Content-Type": "multipart/form-data" },
        withCredentials: true,
      });
      alert("상품 등록 완료!");
    } catch (error) {
      console.error(error);
      alert("상품 등록 실패");
    }
  }

  return (
    <form onSubmit={handleSubmit}>

      <input type="text" placeholder="상품명"
        onChange={(e) => setItemDto({ ...itemDto, itemTitle: e.target.value })} />

      <input type="number" placeholder="가격"
        onChange={(e) => setItemDto({ ...itemDto, itemPrice: e.target.value })} />

      <textarea placeholder="상세 설명"
        onChange={(e) => setItemDto({ ...itemDto, itemDetail: e.target.value })} />

      <input type="number" placeholder="재고"
        onChange={(e) => setItemDto({ ...itemDto, itemSize: e.target.value })} />

      <input type="file"
        onChange={(e) => setFile(e.target.files[0])} />

      <button type="submit">등록하기</button>
    </form>
  );

}

export default AdminAddItemContainer