import axios from 'axios'
import React, { useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'

const MyCrewJoinRequestContainer = () => {
  const {crewId} = useParams()
  const [myCrewJoinRequestList , setMyCrewJoinRequestList] = useState([])
  

  // 크루가입요청 리스트
  const MyCrewjoinRequest = async () => {
    try {
      const res = await axios.get(`/api/mycrew/${crewId}/joinRequest`)
      console.log(res.data)
      setMyCrewJoinRequestList(res.data.myCrewJoinList)
      
    } catch (error) {
      console.log("내 크루 가입요청 get 실패")
      alert("내 크루 가입요청 get 실패")
    }
  }

  
  useEffect(()=> {
    MyCrewjoinRequest(1);
  }, [])


  //검색 값 Change
  const onKeywordChange = (e) => {
    setKeyword(e.target.value)
  }


  //크루가입승인
  const onJoinApproved = async (joinReq) =>{
      try {
        const res = await axios.post(`/api/mycrew/${crewId}/joinRequest/approved`,
          { crewRequestId: crewId,
            memberRequestId: joinReq.memberRequestId,
            message: joinReq.message },
          { headers: { "Content-Type": "application/json" } }

        )
        console.log(res.data)
        
      } catch (error) {
        console.log("내 크루 가입승인 실패")
        alert("내 크루 가입승인 실패")
      }
      MyCrewjoinRequest();
  }
  //크루가입거절
  const onJoinRejected =async (joinReq) =>{

      try {
        const res = await axios.post(`/api/mycrew/${crewId}/joinRequest/rejected`,
          { crewRequestId: crewId,
            memberRequestId: joinReq.memberRequestId,
            message: joinReq.message},
          { headers: { "Content-Type": "application/json" } }

        )
        console.log(res.data)
        
      } catch (error) {
        console.log("내 크루 가입거절 실패")
        alert("내 크루 가입거절 실패")
      }
      MyCrewjoinRequest()     
  }
  
  return (
    <div className="myCrewJoin">
      <div className="myCrewJoin-con">
        <div>MyCrewJoinRequestContainer {crewId}</div>

            <ul>
          {myCrewJoinRequestList.map((joinReq)=>(
              <li key={joinReq.id}>

              {/* 넣고 싶은 정보 더 넣으면 됨 dto에 안한거임 */}
              <span>ID : {joinReq.id}</span>
              <span>회원 ID : {joinReq.memberRequestId}</span>
              <span>가입 메시지 : {joinReq.message}</span>
              <span>상태 :  {joinReq.status}</span>
              <button type="button" onClick={()=>onJoinApproved(joinReq)}>가입승인</button>
              <button type="button" onClick={()=>onJoinRejected(joinReq)}>가입거절</button>
              </li>

            ))}
          </ul>
      </div>
    </div>
  )
    
}

export default MyCrewJoinRequestContainer