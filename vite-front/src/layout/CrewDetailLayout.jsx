import React, { useEffect, useState } from 'react'
import { NavLink, Outlet, useNavigate, useParams } from 'react-router-dom'
import axios from 'axios'

const CrewDetailLayout = (props) => {
  const { id } = useParams();
  const [crew, setCrew] = useState({});
  const navigate = useNavigate()

  useEffect(() => {
    const crewDetail = async (id) => {
        try {
            const res = await axios.get(`/api/crew/detail/${id}`)
            setCrew(res.data.crewDetail)
            console.log(res.data.crewDetail) 
        } catch (err) {
            console.error("크루 상세 실패", err)
        }
    }
    crewDetail(id);
  }, [id])
  
  let images;

  return (
    <>
      <div className="crew">
        <div className="crew-con">
          <div className="crew-nav">
            <div className="crew-nav-menu">
              <ul>
                <li>
                  <NavLink to={`/crew/detail/${id}`} 
                    end className={({ isActive }) => isActive ? 'home active' : 'home'}>
                      홈
                  </NavLink>
                </li>
                <li>
                  <NavLink to={`/crew/board/${id}`} 
                    end className={({ isActive }) => isActive ? 'board active' : 'board'}>
                      게시판
                  </NavLink>
                </li>
                <li>
                  <NavLink to={`/crew/photo/${id}`} 
                    end className={({ isActive }) => isActive ? 'photo active' : 'photo'}>
                      사진
                  </NavLink>
                </li>
                <li>
                  <NavLink to={`/crew/chat/${id}`} 
                    end className={({ isActive }) => isActive ? 'chat active' : 'chat'}>
                      채팅
                  </NavLink>
                </li>
              </ul>
            </div>
          </div>


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


          <Outlet/>
        </div>
      </div>
    </>
  )
}

export default CrewDetailLayout