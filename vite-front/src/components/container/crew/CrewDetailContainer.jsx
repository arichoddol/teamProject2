import axios from 'axios';
import React, { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom';
import CrewDetailLayout from '../../../layout/CrewDetailLayout';

const CrewDetailContainer = () => {
  const { id } = useParams();
  const [crew, setCrew] = useState({}); // 초기값 null
  const navigate = useNavigate();

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
    fetchCrewDetail(id);
  }, [id]);

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
        </div>
      </div>
    // </CrewDetailLayout>
  );
}

export default CrewDetailContainer