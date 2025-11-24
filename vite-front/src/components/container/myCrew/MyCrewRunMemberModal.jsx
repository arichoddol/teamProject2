import React, { useState } from 'react'

const MyCrewRunMemberModal = ({input, onClose, onMember,runId, nowPage, startPage, endPage, totalPages}) => {

  return (
    <div className='myCrewModal'>
        <div className='myCrewModal-con'>
            <div className="modal-header">
                <div><h2>크루런닝 스케줄 참가자리스트</h2></div>
                <button type='button' onClick={onClose}>X</button>
            </div>
            <div className="modal-body">
                {/* 필요한 정보는 더 넣으면 됨 근데 일단 걍 이것만 넣음 */}
            <ul>
            {input.map((runMember)=>(
              <li key={runMember.id}>
              {/* 넣고 싶은 정보 더 넣으면 됨 dto에 안한거임 */}
              <span>참가순서 : {runMember.id}</span>
              <span>회원 ID : {runMember.memberId}</span>
              <span>회원 닉네임 : {runMember.memberNickName}</span>
              </li>
            ))}
          </ul>
            </div>
        <div className="myCrew-paging">
            <div className="myCrew-paging-con">
              <ul>
                <li>
                <li>총페이지{totalPages}</li>
                <button
                  disabled={nowPage === 1}
                  onClick={() => onMember(runId,nowPage - 2)}>
                  이전
                </button>
                </li>

                <li>
                {Array.from(
                  { length: endPage - startPage + 1 },
                  (_, idx) => startPage + idx
                ).map((pageNum) => (
                  <button
                  key={pageNum}
                  onClick={() => onMember(runId,pageNum - 1)}
                  className={pageNum === nowPage ? "now" : ""}
                  >
                    {pageNum}
                  </button>
                ))}
                </li>

                <li>
                <button
                  disabled={nowPage === totalPages}
                  onClick={() => onMember(runId,nowPage)}>
                  다음
                </button>
                </li>
            
              </ul>
            </div>
          </div>
        </div>
    </div>
  )
}

export default MyCrewRunMemberModal