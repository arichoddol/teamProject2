import React, { useEffect, useState } from "react";
import jwtAxios from "../../../apis/util/jwtUtil";
import { BACK_BASIC_URL } from "../../../apis/commonApis";
import { useSelector } from "react-redux";

const AdminCrewListContainer = () => {
  const accessToken = useSelector((state) => state.jwtSlice.accessToken);
  const [adminCrewList, setAdminCrewList] = useState([]);

  const adminCrewListFn = async () => {
    try {
      const res = await jwtAxios.get(
        `${BACK_BASIC_URL}/api/admin/crew/crewList`,
        {
          headers: { Authorization: `Beaer ${accessToken}` },
          withCredentials: true,
        }
      );
      setAdminCrewList(res.data.content);
      console.log(res.data);
    } catch (err) {
      console.log("크루 목록 조회를 실패했습니다. " + err);
    }
  };

  useEffect(() => {
    adminCrewListFn();
  }, []);

  return (
    <>
      <div className="adminCrewLIst">
        <div className="adminCrewList-con">
          <h2>크루목록</h2>
          <table>
            <thead>
              <tr>
                <th>번호</th>
                <th>크루명</th>
                <th>리더</th>
                <th>크루인원</th>
                <th>생성시간</th>
                <th>상세보기</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                {adminCrewList.map((el) => {
                  return (
                    <>
                      <td>{el.id}</td>
                      <td>{el.name}</td>
                      <td></td>
                      <td></td>
                      <td></td>
                      <td></td>
                    </>
                  );
                })}
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </>
  );
};

export default AdminCrewListContainer;
