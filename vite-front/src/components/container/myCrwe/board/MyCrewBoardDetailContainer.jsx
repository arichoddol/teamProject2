import React, { useEffect, useState } from 'react'
import { useSelector } from 'react-redux';
import { useNavigate, useParams } from 'react-router-dom'

const MyCrewBoardDetailContainer = () => {
  const { id, crewId } = useParams();
  const navigate = useNavigate();
  const { accessToken } = useSelector((state) => state.jwtSlice);

  const [board, setBoard] = useState({});
  const [comment, setComment] = useState("");
  const [comments, setComments] = useState([])

  useEffect(() => {
    const fetchBoard = async () => {
      try {
        const res = await axios.get(`/api/mycrew/${crewId}/board/detail/${id}`);
        setBoard(res.data.boardDetail);
        console.log(res.data.boardDetail);
      } catch (err) {
        console.error("크루 보드 상세 실패", err)
      }
    };
  }, [crewId, id])

  return (
    <div className="crewBoardDetail">
      <div className="crewBoardDetail-con">
        <ul>
          <li className="image">
            {board.crewBoardImageEntities && board.crewBoardImageEntities.legth > 0 && (
              <div className="images">
                {board.crewBoardImageEntities.map((img, index) => (
                  <img
                    key={index} 
                    src={img.newName} 
                    alt={`${board.title} 이미지 ${index + 1}`}
                    className='crewBoardImg'
                  />
                ))}
              </div>
            )}
          </li>
          <li class="s1">
            <span>제목</span>
            <span class="s2" th:text="${board.title}"></span>
          </li>
          <li class="s1">
            <span>내용</span>
            <span class="s2" th:text="${board.content}"></span>
          </li>
          <li class="s1">
            <span>글작성자</span>
            <span class="s2" th:text="${board.memberNickName}"></span>
          </li>
          <li class="s1">
            <button onClick={() => navigate(`/mycrew/${crewId}/board/create`)}>글작성</button>
            <button onClick={() => navigate(`/mycrew/${crewId}/board/list`)}>글목록</button>
            <button onClick={() => navigate(`/mycrew/${crewId}/board/delete/${id}`)}>삭제</button>
          </li>
        </ul>
      </div>
    </div>
  )
}

export default MyCrewBoardDetailContainer