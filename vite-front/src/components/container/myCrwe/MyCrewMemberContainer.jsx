import axios from 'axios'
import React, { useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import MyCrewMemberDetailModal from './MyCrewMemberDetailModal'

const MyCrewMemberContainer = () => {
  const {crewId} = useParams()
  const [ myCrewMemberList, setMyCrewMemberList] = useState([])
  const [ detailOpen, setDetailOpen] = useState(false);
  const [ detailData, setDetailData] = useState(null);

 
  // 보이고 싶은 정보 상의 후 추가
  const onMyCrewMemberList = async() =>{
    try {
      const res = await axios.get(`/api/mycrew/${crewId}/member`)
      console.log(res.data.crewMember)
      setMyCrewMemberList(res.data.crewMember)
      
    } catch (error) {
      console.log("내크루원 리스트 get실패")
    }
  }
  useEffect(()=>{
    onMyCrewMemberList()
  }, [])

  const onCrewMemberDetail = async (crewMember) => {
    try {
      const crewMemberId = crewMember.memberId
      console.log(crewMemberId)
      const res = await axios.get(`/api/mycrew/${crewId}/member/detail/${crewMemberId}`)
      console.log(res.data.crewMember)
      setDetailData(res.data.crewMember)
      setDetailOpen(true)
    } catch (error) {
      console.log("내크루원 디테일 get실패")
    }
    onMyCrewMemberList();
  }

  const onCrewMemberDelete = async (crewMember) => {
    try {
      const crewMemberTbId = crewMember.id
      console.log(crewMemberTbId)
      const res = await axios.get(`/api/mycrew/${crewId}/member/delete/${crewMemberTbId}`)
      console.log(res.data)
      alert(res.data)
      
    } catch (error) {
      console.log("내크루원 삭제 get실패")
    }
    onMyCrewMemberList();
  }
  return (
    <div className="myCrewMember">
      <div className="myCrewMember-con">
      <div>MyCrewMemberContainer {crewId}</div>
        <ul>
          {myCrewMemberList.map((crewMember)=>(
          <li key={crewMember.id}>
            <span>회원 ID : {crewMember.id}</span>
            <span>크루 ID : {crewMember.crewId}</span>
            <span>회원 ID : {crewMember.memberId}</span>
            <span>권한 :  {crewMember.roleInCrew}</span>
            
            {/* 크루 리더면, 탈퇴는 로그인 아이디랑 맞으면 보이게 */}
            {/* 더 보이고 싶은 정보 dto에 추가 ㄱㄱ */}
            <button type="button" onClick={()=>onCrewMemberDetail(crewMember)}>자세히 보기</button>
            <button type="button" onClick={()=>onCrewMemberDelete(crewMember)}>탈퇴</button>
          </li>
          ))}
        </ul>
      </div>
          {/* 디테일 모달 ->  마찬가지로 더 보이고 싶은 정보 dto에 추가한거 가져다가 쓰면됨 */}
          {detailOpen && (
            <MyCrewMemberDetailModal
            detail={detailData}
            onClose={()=> {
              setDetailOpen(false)
              setDetailData(null)
              onMyCrewMemberList()}}
            onDelete={()=>onCrewMemberDelete(crewMemberTbId)}
            />
          )}
    </div>

  )
}

export default MyCrewMemberContainer