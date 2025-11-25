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

        console.log(res.data)
        setMyCrew(res.data.crew)

      } catch (error) {
        console.log("내 크루 get 실패");
        // alert("내 크루 get 실패")
      }
    }
    myCrewMain();
  }, [])

  console.log(myCrew.memberId)
  return (
    <div className="myCrewMain">
      <div className="myCrewMain-con">
        <div className="myCrewMain-inner">
          <div className="myCrewMain-title">
            MyCrewMainContainer {crewId}
          </div>
  
          {myCrew.newFileName && myCrew.newFileName.length > 0 && (
            <div className="myCrewMainImage">
              <img
                src={myCrew.newFileName[0]}
                alt={`${myCrew.name} 이미지`}
                className="crewImage"
              /> 
            </div>
          )}
  
          <ul className="myCrewMain-list">
            <li className="myCrewMain-row myCrewMain-row-id" key={myCrew.id}>
              <span>{myCrew.id}</span>
            </li>
  
            <li className="myCrewMain-row myCrewMain-row-name">
              {myCrew.name}
            </li>
  
            <li className="myCrewMain-row myCrewMain-row-desc">
              {myCrew.description}
            </li>
  
            <li className="myCrewMain-row myCrewMain-row-district">
              {myCrew.district}
            </li>
  
            {myCrew.memberId === loginMemberId && (
              <li className="myCrewMain-row myCrewMain-row-edit">
                <button
                  className="myCrewMain-editBtn"
                  onClick={() => navigate(`/mycrew/${crewId}/update`)}
                >
                  수정
                </button>
              </li>              
            )}
          </ul>
        </div>
      </div>
    </div>
  );
  
}

export default MyCrewMainContainer