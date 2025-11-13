import React from 'react'
import { Link } from 'react-router-dom'

const MyCrewLeftContainer = () => {
  return (
    <div className='MyCrewLeftContainer'>
      <div className='MyCrewLeftContainer-con'>
        <ul>
          <li>
            <Link to={""}>CREW HOME</Link>
          </li>

          <li>
            <Link to={"join"}>가입신청명단</Link>
          </li>

          <li>
            <Link to={"member"}>크루원</Link>
          </li>

          <li>
            <Link to={"run"}>런닝스케줄</Link>
          </li>

          <li>
            <Link to={"board"}>크루게시글</Link>
          </li>
            <Link to={"chat"}>크루채팅</Link>
          <li>

          </li>
        </ul>

      </div>
    </div>
  )
}

export default MyCrewLeftContainer