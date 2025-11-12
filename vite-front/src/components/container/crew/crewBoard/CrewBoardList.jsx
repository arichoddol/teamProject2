import axios from 'axios';
import React, { useEffect, useState } from 'react'
import { Link } from 'react-router';

const CrewBoardList = () => {
  
  const [crewBoardList, setCrewBoardList] = useState([]);
  
  useEffect(() => {
    axios.get(`/api/crewBoard/list`)
      .then((res) => {
        setCrewBoardList(res.data.crewBoardList);
        console.log(res.data.crewBoardList);
      })
      .catch((err) => {
        console.error("크루 게시글 목록 실패", err)
      })
  }, [])

  return (
    <>
    <div className="crewBoardList">
        <div className="crewBoardList-con">
            <h1>크루 게시글 목록</h1>
            <ul>
                {crewBoardList.map((board) => {
                    const formattedDate = new Date(board.createTime).toLocaleString('ko-KR', {
                        year: 'numeric',
                        month: 'long',
                        day: 'numeric',
                        hour: 'numeric',
                        minute: '2-digit',
                        hour12: true
                    });
                    return (
                    <li key={board.id}>
                        <Link to={`/crew/crewBoard/detail/${board.id}`}>
                            <div className="boardContent">
                                <div className="name">
                                    {board.memberEntity.nickName}
                                    {formattedDate}
                                    {board.title}
                                </div>
                            </div>
                        </Link>
                    </li>
                    );
                })}
            </ul>
        </div>
    </div>
    </>
  )
}

export default CrewBoardList