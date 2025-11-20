import React, { useEffect, useState } from "react";
import jwtAxios from "../../../../apis/util/jwtUtil";
import { BACK_BASIC_URL } from "../../../../apis/commonApis";
import { useParams } from "react-router";
import { useSelector } from "react-redux";
import { formatDate } from "../../../../js/formatDate";

const CrewRequestModal = ({ isModal, setIsModal, crewRequestId }) => {
  const accessToken = useSelector((state) => state.jwtSlice.accessToken);
  const [crewRequestDetail, setCrewRequestDetail] = useState({});
  const [crewStatus, setCrewStatus] = useState();

  console.log(crewStatus);
  const modalClick = () => {
    setIsModal(false);
  };

  const adminCrewRequestDetailFn = async () => {
    try {
      const res = await jwtAxios.get(
        `${BACK_BASIC_URL}/api/admin/crew/create/detail/${crewRequestId}`,
        {
          headers: { Authorization: `Bearer ${accessToken}` },
          withCredentials: true,
        }
      );
      setCrewRequestDetail(res.data);
      setCrewStatus(res.data.status);
    } catch (err) {
      console.log("크루 상세 조회를 실패하였습니다. " + err);
    }
  };

  const crewApproved = async () => {
    try {
      await jwtAxios.post(
        `${BACK_BASIC_URL}/api/crew/create/request/approved`,
        {},
        {
          headers: { Authorization: `Bearer ${accessToken}` },
          withCredentials: true,
        }
      );
    } catch (err) {
      console.log("수락 요청을 실패했습니다. " + err);
    }
  };

  const crewRejected = async () => {
    try {
      await jwtAxios.post(
        `${BACK_BASIC_URL}/api/crew/create/request/rejected`,
        {},
        {
          headers: { Authorization: `Bearer ${accessToken}` },
          withCredentials: true,
        }
      );
    } catch (err) {
      console.log("거절 요청을 실패했습니다. " + err);
    }
  };

  useEffect(() => {
    adminCrewRequestDetailFn();
  }, []);

  console.log(crewRequestDetail);
  return (
    <>
      <div className="adminCrewDetail">
        <div className="adminCrewDetail-con">
          <span onClick={modalClick}>X</span>
          <h3>{crewRequestDetail.crewName}</h3>
          <ul>
            <li>{crewRequestDetail.district}</li>
            <li>{crewRequestDetail.message}</li>
            <li>{crewRequestDetail.status}</li>
            <li>{formatDate(crewRequestDetail.createTime)}</li>
            {crewStatus == "APPROVED" ? null : (
              <li>
                <button onClick={crewApproved}>수락</button>
                <button onClick={crewRejected}>거절</button>
              </li>
            )}
          </ul>
        </div>
      </div>
    </>
  );
};

export default CrewRequestModal;
