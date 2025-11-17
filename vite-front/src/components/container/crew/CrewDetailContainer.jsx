import axios from 'axios';
import React, { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom';
import CrewDetailLayout from '../../../layout/CrewDetailLayout';
import CrewJoinRequestModal from './CrewJoinRequestModal';
import { useSelector } from 'react-redux';

const CrewDetailContainer = () => {
  const { crewId } = useParams();
  const [crew, setCrew] = useState({}); // 초기값 null
  const navigate = useNavigate();
  const loginMemberId = useSelector(state => state.loginSlice.id)

  useEffect(() => {
    const fetchCrewDetail = async (crewId) => {
      try {
        const res = await axios.get(`/api/crew/detail/${crewId}`);
        setCrew(res.data.crewDetail);
        console.log(res.data.crewDetail);
      } catch (err) {
        console.error("크루 상세 실패", err);
      }
    };
    fetchCrewDetail(crewId);
  }, [crewId]);
  
  // 크루 가입 데이터
  const crewJoinRequestData = {
    crewRequestId: crewId,
    memberRequestId: loginMemberId,
    message: "",
  }

  // 모달, 데이터 상태관리
  const [ joinRequestData, setJoinRequestData ] = useState(crewJoinRequestData)
  const [ joinRequestModal, setJoinRequestModal ] = useState(false)

  // input 데이터로 변환
  const onInputChange = async (e) => {
    const name = e.target.name;
    const value = e.target.value;
    console.log(name, value)

    setJoinRequestData({ ...joinRequestData, [name]: value });
  }

  // 가입 함수
  const onJoinRequest = async () => {
    try {
      const res = await axios.post(`/api/crew/joinRequest`,
        joinRequestData,
        { headers: { "Content-Type": "application/json" }}
      )      
      console.log(res.data)
    } catch (error) {
      alert('크루 가입 요청 보내기 실패')
    }
  }  

  return (
    // <CrewDetailLayout>
      <div className="crewDetailHome">
        <div className="crewDetailHome-con">
          <div className="image">
          {crew.crewImageEntities?.length > 0 ? (
              <img
                src={crew.crewImageEntities[0].newName}
                alt={`${crew.name} 이미지`}
                className='crewImage'
              />
            ) : (
              <div>이미지 없음</div>
            )}
          </div>
          <div className="introduction">
            <h2>{crew.name}</h2>
            <p>{crew.description}</p>
            <p>{crew.district ?? "없음"}</p>
            <p>멤버 {crew.crewMemberEntities?.length ?? 0}명</p>
          </div>
          <button type='button' onClick={() => setJoinRequestModal(true)}>가입신청</button>
        </div>
        {joinRequestModal &&
            (<CrewJoinRequestModal
                onCrew={crew}
                input={joinRequestData}
                onClose={() => setJoinRequestModal(false)}
                onSubmit={onJoinRequest}
                onChange={onInputChange}
              />
            )}
      </div>
    // </CrewDetailLayout>
  );
}

export default CrewDetailContainer