import axios from 'axios';
import React, { useEffect, useState } from 'react'
import { Link, useNavigate, useParams } from 'react-router-dom';

const MyCrewBoardContainer = () => {
  const { crewId } = useParams();
  const navigate = useNavigate();
  const [crewBoardList, setCrewBoardList] = useState([]);
  

  const [page, setPage] = useState(0);
  const [size] = useState(10);
  const [totalPages, setTotalPages] = useState(0);
  const [keyword, setKeyword] = useState('');

  useEffect(() => {
    if (!crewId) return; 

    axios.get(`/api/mycrew/${crewId}/board/list`, {
        params: { page, size, keyword } 
    })
      .then((res) => {
        setCrewBoardList(res.data.crewBoardList.content);
        setTotalPages(data.totalPages);
        console.log(res.data.crewBoardList.content);
      })
      .catch((err) => {
        console.error("크루 게시글 목록 실패", err)
      })
  }, [crewId, page, keyword])

  const create = () => navigate(`/mycrew/${crewId}/board/create/`)

  return (
    <>
    <div className="crewBoardList">
        <div className="crewBoardList-con">
            {/* <h1>{crewBoardList.crewEntity.name} 게시판</h1> */}
            <button className='createCrewBoard' onClick={create}>게시글 작성</button>
            {crewBoardList.length === 0 ? (
              <p>등록된 게시글이 없습니다.</p>
            ) : (
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
                        <Link to={`/mycrew/${crewId}/board/detail/${board.id}`}>
                            <div className="boardContent">
                                <div className="name">
                                    {board.memberNickName}
                                    {board.title}
                                    {formattedDate}
                                </div>
                            </div>
                        </Link>
                    </li>
                    );
                })}
            </ul>
            )}

          <div className="pagination">
          {Array.from({ length: totalPages }).map((_, idx) => (
            <button 
              key={idx} 
              onClick={() => setPage(idx)}
              disabled={idx === page}
            >
              {idx + 1}
            </button>
          ))}
        </div>
        </div>
    </div>
    </>
  )
}

export default MyCrewBoardContainer