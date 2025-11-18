import axios from 'axios';
import React, { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'

const CrewMainContainer = () => {

  const [crewList, setCrewList] = useState([]);

  useEffect(() => {
    axios.get(`/api/crew/list`)
      .then((res) => {
        setCrewList(res.data.crewList);
        console.log(res.data.crewList);
      })
      .catch((err) => {
        console.error("크루 목록 실패", err)
      })
  }, [])
  
  
  return (
    <>
    <div className="crewList">
      <div className="crewList-con">
        <h1>크루 목록</h1>
        <ul>
          {crewList.map((crew) => {
            const images = crew.crewImageEntities || [];
            return (
              <li key={crew.id}>
                <Link to={`/crew/detail/${crew.id}`}>
                  <div className="crewListLeft">
                    {images.length > 0 ? (
                      <img 
                        src={images[0].newName} 
                        alt={`${crew.name} 이미지`}
                        className='crewImage'
                      />
                    ) : (
                      <div>이미지 없음</div>
                    )}
                  </div>
                  <div className="crewListRight">
                    <h2>{crew.name}</h2>
                    <p>{crew.description}</p>
                    <p>{crew.district}</p>
                  </div>
                </Link>
              </li>
            )
          })}
        </ul>
      </div>
    </div>
    </>
  )
}

export default CrewMainContainer