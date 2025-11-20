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
  const [startPage, setStartPage] = useState(0)
  const [endPage, setEndPage] = useState(0)
  const [hasNext, setHasNext] = useState(false)
  const [hasPrevious, setHasPrevious] = useState(false)

  const [keyword, setKeyword] = useState('');
  const [subject, setSubject] = useState('전체')

  const formattedDate = (el) => new Date(el).toLocaleString('ko-KR', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    hour: 'numeric',
    minute: '2-digit',
    hour12: true
  });

  
  const boardList = async () => {
    try {
      const res = await axios.get(`/api/mycrew/${crewId}/board/list`, {
        params: { page, size, keyword, subject },
      })
      const data = res.data.crewBoardList
        setCrewBoardList(data.content || []);
        setTotalPages(data.totalPages || 0);
        setStartPage(data.startPage)
        setEndPage(data.endPage)
        setHasNext(data.hasNext)
        setHasPrevious(data.hasPrevious)
    } catch(err) {
      console.error("크루 게시글 목록 실패", err)
    }
  }

  useEffect(() => {
    if (!crewId) return;
    boardList();
  }, [crewId, page, keyword, subject])

  const search = (e) => {
    e.preventDefault();
    setPage(0);
    boardList();
  }

  useEffect(() => {
    setPage(0)
  }, [crewId])

  const create = () => navigate(`/mycrew/${crewId}/board/create/`)

  return (
    <>
    <div className="crewBoardList">
        <div className="crewBoardList-con">
            <h1>{crewBoardList.crewName} 게시판</h1>

            <form onSubmit={search}>
              <select name="crewBoard" id="crewBoard" value={subject} onChange={(e) => setSubject(e.target.value)}>
                <option value="전체">전체</option>
                <option value="제목">제목</option>
                <option value="내용">내용</option>
                <option value="작성자">작성자</option>
              </select>
              <input 
                type="text" 
                placeholder='검색'
                value={keyword}
                onChange={(e) => setKeyword(e.target.value)}
              />
              <button type='submit'>검색</button>
            </form>

            <button className='createCrewBoard' onClick={create}>게시글 작성</button>

            {crewBoardList.length === 0 ? (
              <p>등록된 게시글이 없습니다.</p>
            ) : (
              <ul>
                {crewBoardList.map((board) => (                    
                    <li key={board.id}>
                        <Link to={`/mycrew/${crewId}/board/detail/${board.id}`}>
                            <div className="boardContent">
                                <div className="crewBoard">
                                  {board.memberNickName}
                                </div>
                                <div className="crewBoard">
                                  {board.title}
                                  {formattedDate(board.createTime)}
                                </div>
                                <div className="crewBoardContet">
                                  
                                </div>
                            </div>
                        </Link>
                    </li>                    
                ))}
            </ul>
            )}

          <div className="crewBoardPagination">
            <button onClick={() => setPage(0)} disabled={page === 0}>처음</button>
            <button onClick={() => setPage(page - 1)} disabled={!hasPrevious}>이전</button>
            {Array.from({ length: endPage - startPage + 1 }, (_, idx) => (
              <button
                key={idx}
                onClick={() => setPage(startPage + idx - 1)}
                disabled={startPage + idx - 1 === page}
              >
                {startPage + idx}
              </button>
            ))}
            <button onClick={() => setPage(page + 1)} disabled={!hasNext}>다음</button>
            <button onClick={() => setPage(totalPages - 1)} disabled={page === totalPages - 1}>마지막</button>
          </div>
        </div>
    </div>
    </>
  )
}

export default MyCrewBoardContainer