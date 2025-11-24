import React from 'react'
import { NavLink, useLocation } from 'react-router-dom'

const MyCrewLeftContainer = () => {

  return (
    <div className='myCrewLeftContainer'>
      <div className='myCrewLeftContainer-con'>
        <ul>
          <li>
            <NavLink to={"index"} 
             className={({ isActive }) => (isActive ? "left-now" : "")}>
            CREW HOME</NavLink>
          </li>
          <li>
            <NavLink to={"join"}
            className={({ isActive }) => (isActive ? "left-now" : "")}>
            가입신청명단</NavLink>

          </li>

          <li>
            <NavLink to={"member"} 
            className={({ isActive }) => (isActive ? "left-now" : "")}>
            크루원</NavLink>
          </li>

          <li>
            <NavLink to={"run"} 
            className={({ isActive }) => (isActive ? "left-now" : "")}>
            런닝스케줄</NavLink>
          </li>

          <li>
            <NavLink to={"board"} 
            className={({ isActive }) => (isActive ? "left-now" : "")}>
            크루게시글</NavLink>
          </li>
          <li>
            <NavLink to={"chat"} 
            className={({ isActive }) => (isActive ? "left-now" : "")}>
            크루채팅</NavLink>

          </li>
        </ul>

      </div>
    </div>
  )
}

export default MyCrewLeftContainer