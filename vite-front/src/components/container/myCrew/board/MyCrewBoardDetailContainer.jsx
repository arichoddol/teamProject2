import React, { useEffect, useState } from 'react'
import { useSelector } from 'react-redux';
import { useNavigate, useParams } from 'react-router-dom'
import axios from 'axios'

const MyCrewBoardDetailContainer = () => {
  const { boardId, crewId } = useParams();
  const navigate = useNavigate();
  const { accessToken } = useSelector((state) => state.jwtSlice);
  const { userEmail } = useSelector((state) => state.loginSlice);
  const loginMemberId = useSelector((state) => state.loginSlice.id)

  const [board, setBoard] = useState({});
  const [comment, setComment] = useState("");
  const [comments, setComments] = useState([]);

  const formattedDate = (el) => new Date(el).toLocaleString('ko-KR', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    hour: 'numeric',
    minute: '2-digit',
    hour12: true
  });
  const formattedDateForComment = (el) => new Date(el).toLocaleString('ko-KR', {
    year: 'numeric',
    month: 'numeric',
    day: 'numeric',
    hour: 'numeric',
    minute: '2-digit',
    hour12: false
  });

  // 상세 게시글
  const fetchBoard = async () => {
    try {
      const res = await axios.get(`/api/mycrew/${crewId}/board/detail/${boardId}`);
      setBoard(res.data.boardDetail || {});
      console.log(res.data.boardDetail);
    } catch (err) {
      console.error("크루 게시글 상세 실패", err)
    }
  };

  // 댓글 목록
  const fetchComments = async () => {
    try {
      const res = await axios.get(`/api/mycrew/${crewId}/board/${boardId}/comment/list`);
      setComments(res.data.commentList);
    } catch (err) {
      console.error("댓글 목록 실패", err)
    }
  }

  useEffect(() => {
    fetchBoard();
    fetchComments();
  }, [boardId, crewId])

  const deleteBoard = async () => {
    if (!window.confirm('게시글을 삭제하시겠습니까?')) return;

    try {
      await axios.delete(`/api/mycrew/${crewId}/board/delete/${boardId}`,
        { headers: {
            Authorization: `Bearer ${accessToken}`
        }}
      );
      navigate(`/mycrew/${crewId}/board/list`)
    } catch (err) {
      console.error("게시글 삭제 실패", err);
    }
  }

  const submitComment = async () => {
    if (!comment.trim()) return;
    try {
      const res = await axios.post(`/api/mycrew/${crewId}/board/${boardId}/comment/write`,
        { content: comment },
        { headers: {
            Authorization: `Bearer ${accessToken}`
        }}
      );
      console.log(res.data);
      setComment(""); 
      fetchComments();
    } catch (err) {
      console.error("댓글 작성 실패", err)
    }
  } 

  const deleteComment = async (commentId, writerId) => {
    if (writerId !== loginMemberId) {
      alert("댓글 작성인만 삭제 가능");
      return;
    }
    try {
      await axios.delete(`/api/mycrew/${crewId}/board/${boardId}/comment/delete/${commentId}`,
        { headers: {
            Authorization: `Bearer ${accessToken}`
        }}
      );
      fetchComments();
      fetchBoard();
    } catch (err) {
      console.error("댓글 삭제 실패", err)
    }
  }

  // const updateComment = async (commentId, comment) => {
  //   try {
  //     await axios.put(`/api/mycrew/${crewId}/board/${boardId}/comment/update/${commentId}`,

  //     )
  //   } catch (err) {

  //   }
  // }

  console.log(loginMemberId)
  console.log(board.memberId)
  console.log(accessToken)

  return (
    <div className="crewBoardDetail">
      <div className="crewBoardDetail-con">
        <ul>
          <li className="image">
            {board.newFileName && board.newFileName.length > 0 && (
              <div className="images">
                {board.newFileName.map((fileName, index) => (
                  <img
                    key={index} 
                    src={`http://localhost:8088/upload/${fileName}`} 
                    alt={`${board.title} 이미지 ${index + 1}`}
                    className='crewBoardImg'
                  />
                ))}
              </div>
            )}
          </li>
          <li className="s1">
            <span>제목</span>
            <span className="s2">{board.title}</span>
          </li>
          <li className="s1">
            <span className="s2">{board.createTime && formattedDate(board.createTime)}</span>
            {board.updateTime &&
            <>
            <span>수정</span>
            <span className="s2">{formattedDate(board.updateTime)}</span> 
            </>
            }
          </li>
          <li className="s1">
            <span>내용</span>
            <span className="s2">{board.content}</span>
          </li>
          <li className="s1">
            <span>글작성자</span>
            <span className="s2">{board.memberNickName}</span>
          </li>
          <li className="s1">
            <button onClick={() => navigate(`/mycrew/${crewId}/board/create`)}>글작성</button>
            <button onClick={() => navigate(`/mycrew/${crewId}/board/list`)}>글목록</button>
          </li>
          {board.memberId === loginMemberId && (
            <li>
              <button onClick={() => navigate(`/mycrew/${crewId}/board/update/${boardId}`)}>수정</button>
              <button onClick={deleteBoard}>삭제</button>
            </li>
          )}
        </ul>
      </div>
      <div className="crewBoardComment">
        <div className="crewBoardComment-con">
          <div className="writeComment">
            <h2 className='crewComment'>댓글</h2>
              <textarea 
                name="comment"
                id='comment'
                value={comment}
                onChange={(e) => setComment(e.target.value)}
                required
                placeholder='내용입력' 
              />
              <button type="button" onClick={submitComment}>댓글 작성</button>
          </div>
          <div className="commentList">
            {comments.length > 0 ? comments.map((comment) => (
              <div key={comment.id} className="aComment">
                <strong>{comment.memberNickName}</strong>
                <span className="commentTime">{comment.createTime && formattedDateForComment(comment.createTime)}</span>
                <div className="commentContent">
                  {comment.content}
                </div>
                {comment.memberId === loginMemberId && (
                <div className="commentBtn">
                  {/* <button onClick={}>수정</button> */}
                  <button onClick={() => deleteComment(comment.id, comment.memberId)}>삭제</button>
                </div>
                )}
              </div>
            )) : <span>댓글이 없습니다.</span>}
          </div>
        </div>
      </div>
    </div>
  )
}

export default MyCrewBoardDetailContainer