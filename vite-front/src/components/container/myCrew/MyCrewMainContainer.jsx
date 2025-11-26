import axios from 'axios';
import React, { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import jwtAxios from '../../../apis/util/jwtUtil';
import { useSelector } from 'react-redux';

const MyCrewMainContainer = () => {
  const accessToken = useSelector(state => state.jwtSlice.accessToken);
  const {crewId} = useParams()
  const [myCrew , setMyCrew] = useState({})

  const navigate = useNavigate()
  const loginMemberId = useSelector((state) => state.loginSlice.id)  

  useEffect(()=> {
    const myCrewMain = async () => {
      try {
        const res = await jwtAxios.get(`/api/mycrew/${crewId}`,
          {
            headers: { Authorization: `Bearer ${accessToken}`},
            withCredentials: true
          }
        );

        console.log(res.data.crew)
        setMyCrew(res.data.crew)

      } catch (error) {
        console.log("내 크루 get 실패");
        // alert("내 크루 get 실패")
      }
    }
    myCrewMain();
  }, [])

  //안하면 데이터 오기전에 있어서 에러남
  const CrewcreatedDate = myCrew.createTime
  ? myCrew.createTime.split("T")[0]
  : "";

  const crewMemberLength =  myCrew.crewMemberEntities
  ? myCrew.crewMemberEntities.length
  : "";

  console.log(myCrew.memberId)
  return (
    <div className="myCrewMain">
      <div className="myCrewMain-con">
        {/* ===== 상단 타이틀 ===== */}
        <div className="myCrewMain-title">
          <div className="myCrewMain-title-left">
            <h2 className="crew-name">
              🏃‍♀️ {myCrew.name || "크루 이름"}
            </h2>
            <p className="crew-district">
              📍 {myCrew.district || "활동 지역 미정"}
            </p>
          </div>

          <div className="myCrewMain-title-right">
            <div className="title-badge">
              <span className="badge-label">📅 창단</span>
              <strong className="badge-value">
                {CrewcreatedDate || "-"}
              </strong>
            </div>
            <div className="title-badge">
              <span className="badge-label">👥 크루원</span>
              <strong className="badge-value">
                {crewMemberLength}명
              </strong>
            </div>
          </div>
        </div>

        {/* ===== 내부 내용 ===== */}
        <div className="myCrewMain-inner">
          {/* 이미지 */}
          {myCrew.newFileName && myCrew.newFileName.length > 0 && (
            <div className="myCrewMainImage">
              <img
                // src={`http://localhost:8088/upload/${myCrew.newFileName[0]}`}
                src={myCrew.fileUrl[0]}
                alt={`${myCrew.name} 이미지`}
                className="crewImage"
              />
            </div>
          )}

          {/* 정보 리스트 */}
          <ul className="myCrewMain-list">
            <div className="top">

            <li className="myCrewMain-row">
              <span className="row-label">👑 크루장</span>
              <span className="row-value">
                {myCrew.memberNickName || "미정"}
              </span>
            </li>

            <li className="myCrewMain-row">
              <span className="row-label">📍 활동 지역</span>
              <span className="row-value">
                {myCrew.district || "미정"}
              </span>
            </li>

            <li className="myCrewMain-row myCrewMain-row-desc">
              <span className="row-label">📝 소개</span>
              <span className="row-value">
                {myCrew.description || "아직 소개글이 없습니다."}
              </span>
            </li>
            </div>
            <div className="bottom">

            {myCrew.memberId === loginMemberId && (
              <div className="myCrewMain-row-edit">
                <button
                  className="myCrewMain-editBtn"
                  onClick={() => navigate(`/mycrew/${crewId}/update`)}
                  >
                  ✏️ 크루 정보 수정
                </button>
              </div>
            )}
            </div>
          </ul>
        </div>
      </div>
    </div>
  );
};


export default MyCrewMainContainer