import React, { useEffect, useState } from "react";
import jwtAxios from "../../../../apis/util/jwtUtil";
import { BACK_BASIC_URL } from "../../../../apis/commonApis";
import { useSelector } from "react-redux";
import AdminPagingComponent from "../../../common/AdminPagingComponent";
import { Link } from "react-router";
import { formatDate } from "../../../../js/formatDate";

const AdminNoticeListContainer = () => {
  const accessToken = useSelector((state) => state.jwtSlice.accessToken);
  const [noticeList, setNoticeList] = useState([]);
  const [pageData, setPageData] = useState({});
  const [currentPage, setCurrentPage] = useState(1);
  const [search, setSearch] = useState("");

  const adminNoticeListFn = async () => {
    try {
      const res = await jwtAxios.get(
        `${BACK_BASIC_URL}/api/admin/board/notice/list`,
        {
          headers: { Authorization: `Bearer ${accessToken}` },
          params: { page: currentPage - 1, size: 10, keyword: search },
          withCredentials: true,
        }
      );
      setNoticeList(res.data.content);
      setPageData(res.data);
    } catch (err) {
      console.log("공지목록 조회 실패,", err);
    }
  };

  const hadlePageChange = (page) => {
    setCurrentPage(page);
  };

  useEffect(() => {
    adminNoticeListFn();
  }, [currentPage]);
  return (
    <div className="admin-notice-list">
      <div className="admin-notice-list-con">
        <div className="admin-notice-list-header">
          <h1>공지 리스트</h1>
          <div className="admin-notice-list-search">
            <input
              value={search}
              onChange={(e) => setSearch(e.target.value)}
              onKeyDown={(e) => {
                if (e.key == "Enter") {
                  adminNoticeListFn();
                }
              }}
              type="text"
              placeholder="검색어를 입력하세요"
            />
            <button onClick={adminNoticeListFn}>검색</button>
          </div>
        </div>

        <div className="admin-notice-list-table-wrapper">
          <table className="admin-notice-list-table">
            <thead>
              <tr>
                <th>번호</th>
                <th>제목</th>
                <th>내용</th>
                <th>작성시간</th>
                <th>보기</th>
              </tr>
            </thead>
            <tbody>
              {noticeList.map((el) => {
                return (
                  <tr key={el.id}>
                    <td>{el.id}</td>
                    <td>{el.title}</td>
                    <td>{el.content}</td>
                    <td>{formatDate(el.createTime)}</td>
                    <td>
                      <Link to={`/admin/notice/detail/${el.id}`}>보기</Link>
                    </td>
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

export default AdminNoticeListContainer;
