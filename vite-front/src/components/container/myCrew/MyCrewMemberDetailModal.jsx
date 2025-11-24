import React from 'react'

const MyCrewMemberDetailModal = ({detail, onClose, onDelete}) => {
    //크루원 속에 데이터가 비었다면?
    if (!detail) return <div>왜 데이터가 없지?</div>
    
  return (
    <div className='myCrewModal'>
        <div className='myCrewModal-con'>
            <div className="modal-header">
                <h2>{detail.memberId}님의 상세정보</h2>
                <button type='button' onClick={onClose}>X</button>
            </div>
            <div className="modal-body">
            <ul>
                <li>테이블아이디 : {detail.id}</li>
                <li>회원아이디 : {detail.memberId}</li>
                <li>크루아이디 : {detail.crewId}</li>
                <li>크루권한 : {detail.roleInCrew}</li>
                <button type='button' onClick={onDelete}>탈퇴</button>
            </ul>
            </div>
        </div>
    </div>
  )
}

export default MyCrewMemberDetailModal