import React from 'react'

const MyCrewRunDetailModal = ({input, onClose, onChange, onSubmit, onDelete, onMember, onRunYes, onRunNo}) => {
    
  return (
    <div className='myCrewRunDetailModal'>
        <div className='myCrewRunDetailModal-con'>
            <div className="modal-header">
                <h2>크루런닝 일정수정</h2>
                <button type='button' onClick={onClose}>X</button>
            </div>
            <div className="modal-body">
            <ul>
                <li>
                    <label htmlFor="id">크루런닝일정ID</label>
                    <input type="text" id='id' name="id" value={input.id} onChange={onChange} readOnly/>
                </li>
                <li>
                    <label htmlFor="crewId">크루ID</label>
                    <input type="text" id='crewId' name="crewId" value={input.crewId} onChange={onChange} readOnly/>
                </li>
                <li>
                    <label htmlFor="memberId">회원ID</label>
                    <input type="text" id='memberId' name="memberId" value={input.memberId} onChange={onChange} readOnly/>
                </li>
                <li>
                    <label htmlFor="startAt">시작날짜</label>
                    <input type="datetime-local" id='startAt' name="startAt" value={input.startAt} onChange={onChange}/>
                </li>
                <li>
                    <label htmlFor="endAt">종료날짜</label>
                    <input type="datetime-local" id='endAt' name="endAt" value={input.endAt} onChange={onChange}/>
                </li>
                <li>
                    <label htmlFor="title">모임 제목</label>
                    <input type="text" id='title' name="title" value={input.title} onChange={onChange}/>
                </li>
                <li>
                    <label htmlFor="place">모임 장소</label>
                    <input type="text" id='place' name="place" value={input.place} onChange={onChange}/>
                </li>
                <li>
                    <label htmlFor="routeHint">짧은 코스</label>
                    <input type="text" id='routeHint' name="routeHint" value={input.routeHint} onChange={onChange}/>
                </li>
                <li>
                    <button type='button' onClick={onSubmit}>일정수정</button>
                    <button type='button' onClick={()=> onMember(input.id)}>일정 참가원</button>
                    <button type='button' onClick={()=> onDelete(input.id)}>일정 삭제</button>
                </li>
                {/* 로그인 유저 아이디 받아서 해야하는데 일단 임시로 그냥 함 */}
                <li>
                    <button type='button' onClick={()=> onRunYes(input.id,input.memberId)}>일정 참가</button>
                    <button type='button' onClick={()=> onRunNo(input.id,input.memberId)}>일정 참가취소</button>
                </li>
            </ul>
            </div>
        </div>
    </div>
  )
}

export default MyCrewRunDetailModal