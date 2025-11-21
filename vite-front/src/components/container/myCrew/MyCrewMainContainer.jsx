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
        <div style={{height: "200vh"}}>MyCrewMainContainer {crewId}
          {myCrew.newFileName && myCrew.newFileName.length > 0 && (
            <div className="myCrewMainImage">
              {myCrew.newFileName.get(0)}
            </div>
          )}
          {/* DTO List<??Entity> 를 그대로 toDto로 받는걸 고쳐야함 
          무한참조나서 데이터를 못가져와요 */}
          <ul>
            {/* {myCrew.map((myCrew)=>( */}
              <li key={myCrew.id}>

              <span>{myCrew.id}</span>
            </li>
              {/* ))} */}
            <li>{myCrew.name}</li>
            <li>{myCrew.description}</li>
            <li>{myCrew.district}</li>
            {myCrew.memberId === loginMemberId && (
              <li>
                <button onClick={() => navigate(`/mycrew/${crewId}/update`)}>수정</button>
              </li>              
            )}
          </ul>
        </div>
      </div>
    </div>
  )
}

export default MyCrewMainContainer