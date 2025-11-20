import React, { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import jwtAxios from "../../../apis/util/jwtUtil";
import { BACK_BASIC_URL } from "../../../apis/commonApis";
import { Link } from "react-router";
import AdminPagingComponent from "../../common/AdminPagingComponent";
import { formatDate } from "../../../js/formatDate";
import CrewRequestModal from "./modal/CrewRequestModal";

const AdminCrewAllowContainer = () => {
  const accessToken = useSelector((state) => state.jwtSlice.accessToken);
  const [crewRequest, setCrewRequest] = useState([]);
  const [crewRequestId, setCrewRequestId] = useState();
  const [pageData, setPageData] = useState({});
  const [currentPage, setCurrentPage] = useState(1);
  const [search, setSearch] = useState();
  const [isModal, setIsModal] = useState(false);

  const crewReqeustFn = async () => {
    try {
      const res = await jwtAxios.get(
        `${BACK_BASIC_URL}/api/admin/crew/create/requestList`,
        {
          headers: { Authorization: `Bearer ${accessToken}` },
          params: { page: currentPage - 1, size: 10, keyword: search },
          withCredentials: true,
        }
      );
      setCrewRequest(res.data.content);
      setPageData(res.data);
    } catch (err) {
      console.log("크루 신청 목록 불러오기 실패 " + err);
    }
  };

  const hadlePageChange = (page) => {
    setCurrentPage(page);
  };

  const modalClick = (id) => {
    setIsModal(true);
    setCrewRequestId(id);
  };

  console.log(crewRequestId);
  console.log(isModal);

  useEffect(() => {
    crewReqeustFn();
  }, [currentPage]);

  return (
    <>
      <div className="admin-crewRequestList">
        <div className="admin-crewRequestList-con">
          <div className="admin-crewRequestList-header">
            <h1>크루신청 리스트</h1>
            <div className="admin-crewRequestList-search">
              <input
                value={search}
                onChange={(e) => setSearch(e.target.value)}
                type="text"
                placeholder="검색어를 입력하세요"
              />
              <button onClick={crewReqeustFn}>검색</button>
            </div>
          </div>

          <div className="admin-crewRequestList-table-wrapper">
            <table className="admin-crewRequestList-table">
              <thead>
                <tr>
                  <th>번호</th>
                  <th>크루명</th>
                  <th>district</th>
                  <th>신청시간</th>
                  <th>상태</th>
                  <th>관리</th>
                </tr>
              </thead>
              <tbody>
                {crewRequest.map((el) => {
                  return (
                    <tr key={el.id}>
                      <td>{el.id}</td>
                      <td>{el.crewName}</td>
                      <td>{el.district}</td>
                      <td>{formatDate(el.createTime)}</td>
                      <td>{el.status}</td>
                      <td>
                        <button onClick={() => modalClick(el.id)}>
                          상세보기
                        </button>
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
      {isModal == true ? (
        <CrewRequestModal
          isModal={isModal}
          setIsModal={setIsModal}
          crewRequestId={crewRequestId}
        />
      ) : null}
    </>
  );
};

export default AdminCrewAllowContainer;
