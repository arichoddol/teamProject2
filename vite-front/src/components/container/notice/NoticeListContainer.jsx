import axios from "axios";
import React, { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import AdminPagingComponent from "../../common/AdminPagingComponent";
import { BACK_BASIC_URL } from "../../../apis/commonApis";
import { formatDate } from "../../../js/formatDate";
import { Link } from "react-router";

const NoticeListContainer = () => {
  const [noticeList, setNoticeList] = useState([]);
  const [pageData, setPageData] = useState({});
  const [currentPage, setCurrentPage] = useState(1);
  const [search, setSearch] = useState("");

  const noticeListFn = async () => {
    try {
      const res = await axios.get(`${BACK_BASIC_URL}/api/notice/noticeList`, {
        params: { page: currentPage - 1, size: 10, keyword: search },
        withCredentials: true,
      });
      setNoticeList(res.data.content);
      setPageData(res.data);
      console.log(res);
    } catch (err) {
      console.log("공지목록 조회 실패,", err);
    }
  };

  const hadlePageChange = (page) => {
    setCurrentPage(page);
  };

  useEffect(() => {
    noticeListFn();
  }, [currentPage]);
  return (
    <div className="board-notice-list">
      <div className="board-notice-list-con">
        <div className="board-notice-list-header">
          <h1>공지사항</h1>
          <div className="board-notice-list-search">
            <input
              value={search}
              onChange={(e) => setSearch(e.target.value)}
              onKeyDown={(e) => {
                if (e.key == "Enter") {
                  noticeListFn();
                }
              }}
              type="text"
              placeholder="검색어를 입력하세요"
            />
            <button onClick={noticeListFn}>검색</button>
          </div>
        </div>

        <div className="board-notice-list-table-wrapper">
          <table className="board-notice-list-table">
            <thead>
              <tr>
                <th>번호</th>
                <th>제목</th>
                <th>작성자</th>
                <th>작성시간</th>
                <th>조회수</th>
              </tr>
            </thead>
            <tbody>
              {noticeList.map((el) => {
                return (
                  <tr key={el.id}>
                    <td>{el.id}</td>
                    <td>
                      <Link to={`/notice/detail/${el.id}`}>{el.title}</Link>
                    </td>
                    <td>관리자</td>
                    <td>{formatDate(el.createTime)}</td>
                    <td>{el.hit}</td>
                  </tr>
                );
              })}
            </tbody>
          </table>
        </div>

        <AdminPagingComponent
          pageData={pageData}
          onPageChange={hadlePageChange}
        />
      </div>
    </div>
  );
};

export default NoticeListContainer;
