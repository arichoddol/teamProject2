import React, { useEffect, useState } from "react";
import "../../../css/api/Marathon_list.css";

const MarathonApiPage = () => {
  const [marathons, setMarathons] = useState([]);

  useEffect(() => {
    fetch("/api/marathons") // 절대경로가 아닌 상대경로
      .then((res) => res.json())
      .then((data) => setMarathons(data))
      .catch((err) => console.error(err));
  }, []);

  return (
    <div className="table-container">
      <h1>국내 마라톤대회 정보</h1>
      <table className="marathon-table" border="1">
        <thead>
          <tr>
            <th>대회명</th>
            <th>일시</th>
            <th>장소</th>
            <th>종목</th>
            <th>주최</th>
          </tr>
        </thead>
        <tbody>
          {marathons.length > 0 ? (
            marathons.map((m, index) => (
              <tr key={index}>
                <td>{m.name}</td>
                <td>{m.date}</td>
                <td>{m.location}</td>
                <td>{m.category}</td>
                <td>{m.host}</td>
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan="5" style={{ textAlign: "center" }}>
                등록된 마라톤 정보가 없습니다.
              </td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  );
};

export default MarathonApiPage;
