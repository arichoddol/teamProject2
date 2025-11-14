import React from 'react'

const MyCrewRunMemberModal = ({input, onClose}) => {
    
  return (
    <div className='myCrewRunMemberModal'>
        <div className='myCrewRunMemberModal-con'>
            <div className="modal-header">
                <h2>크루런닝 스케줄 참가자리스트</h2>
                <button type='button' onClick={onClose}>X</button>
            </div>
            <div className="modal-body">
                {/* 필요한 정보는 더 넣으면 됨 근데 일단 걍 이것만 넣음 */}
            <ul>
            {input.map((runMember)=>(
              <li key={runMember.id}>
              {/* 넣고 싶은 정보 더 넣으면 됨 dto에 안한거임 */}
              <span>ID : {runMember.crewRunId}</span>
              <span>회원 ID : {runMember.memberId}</span>
              <span>회원 닉네임 : {runMember.memberNickName}</span>
              </li>
            ))}
          </ul>
            </div>
        </div>
    </div>
  )
}

export default MyCrewRunMemberModal