import React, { useEffect, useState } from "react";

import { useSelector } from "react-redux";
import { formatDate } from "../../../../js/formatDate";
import AdminPagingComponent from "../../../common/AdminPagingComponent";
import jwtAxios from "../../../../apis/util/jwtUtil";
import { BACK_BASIC_URL } from "../../../../apis/commonApis";
import CrewDetailModal from "../modal/CrewDetailModal";

const AdminCrewListContainer = () => {
  const accessToken = useSelector((state) => state.jwtSlice.accessToken);
  const [ademinCrewDetailId, setAdeminCrewDetailId] = useState();
  const [isModal, setIsModal] = useState(false);
  const [adminCrewList, setAdminCrewList] = useState([]);
  const [pageData, setPageData] = useState({});
  const [currentPage, setCurrentPage] = useState(1);
  const [search, setSearch] = useState();

  const adminCrewListFn = async () => {
    try {
      const res = await jwtAxios.get(
        `${BACK_BASIC_URL}/api/admin/crew/crewList`,
        {
          headers: { Authorization: `Bearer ${accessToken}` },
          params: { page: currentPage - 1, size: 10, keyword: search },
          withCredentials: true,
        }
      );
      setAdminCrewList(res.data.content);
      setPageData(res.data);
      console.log(res.data.content);
    } catch (err) {
      console.log("크루 목록 조회를 실패했습니다. " + err);
    }
  };

  console.log(search);

  const hadlePageChange = (page) => {
    setCurrentPage(page);
  };

  const modalClick = (id) => {
    setIsModal(true);
    setAdeminCrewDetailId(id);
  };

  useEffect(() => {
    adminCrewListFn();
  }, [currentPage]);

  return (
    <>
      <div className="admin-crewList">
        <div className="admin-crewList-con">
          <div className="admin-crewList-header">
            <h1 onClick={adminCrewListFn}>크루 리스트</h1>
            <div className="admin-crewList-search">
              <input
                value={search}
                onChange={(e) => setSearch(e.target.value)}
                onKeyDown={(e) => {
                  if (e.key == "Enter") {
                    adminCrewListFn();
                  }
                }}
                type="text"
                placeholder="검색어를 입력하세요"
              />
              <button onClick={adminCrewListFn}>검색</button>
            </div>
          </div>

          <div className="admin-crewList-table-wrapper">
            <table className="admin-crewList-table">
              <thead>
                <tr>
                  <th>번호</th>
                  <th>크루명</th>
                  <th>리더</th>
                  <th>크루인원</th>
                  <th>지역</th>
                  <th>생성시간</th>
                  <th>상세보기</th>
                </tr>
              </thead>
              <tbody>
                {adminCrewList.map((el) => {
                  return (
                    <tr key={el.id}>
                      <td>{el.id}</td>
                      <td>{el.name}</td>
                      <td>{el.memberNickName}</td>
                      <td>{el.crewMemberEntities.length}명</td>
                      <td>{el.district}</td>
                      <td>{formatDate(el.createTime)}</td>
                      <td>
                        <button
                          onClick={() => {
                            modalClick(el.id);
                          }}
                        >
                          보기
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
        <CrewDetailModal
          isModal={isModal}
          setIsModal={setIsModal}
          ademinCrewDetailId={ademinCrewDetailId}
        />
      ) : null}
    </>
  );
};

export default AdminCrewListContainer;
