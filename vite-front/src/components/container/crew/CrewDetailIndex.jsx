import axios from 'axios';
import React, { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom';
import CrewDetailLayout from '../../../layout/CrewDetailLayout';

const CrewDetailIndex = () => {
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
    <CrewDetailLayout>
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
          <h2>{crew.name}</h2>
          <p>{crew.description}</p>
        </div>
      </div>
    </CrewDetailLayout>
  );
}

export default CrewDetailIndex