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

  const [page, setPage] = useState(0);
  const [size] = useState(20);
  const [totalPages, setTotalPages] = useState(0);
  const [startPage, setStartPage] = useState(0)
  const [endPage, setEndPage] = useState(0)
  const [hasNext, setHasNext] = useState(false)
  const [hasPrevious, setHasPrevious] = useState(false)
  

  const [board, setBoard] = useState({});
  const [totalComments, setTotalComments] = useState(0);
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
      const res = await axios.get(`/api/mycrew/${crewId}/board/${boardId}/comment/list`,{
        params: {page, size}
      });
      const data = res.data.commentList
      setComments(data.content || []);
      setTotalPages(data.totalPage || 0)
      setStartPage(data.startPage)
      setEndPage(data.endPage)
      setHasNext(data.hasNext)
      setHasPrevious(data.hasPrevious)
      setTotalComments(data.totalElements || 0)
    } catch (err) {
      console.error("댓글 목록 실패", err)
    }
  }

  useEffect(() => {
    fetchBoard();
    fetchComments();
  }, [boardId, crewId, page])

  useEffect(() => {
    setPage(0)
  }, [crewId])

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
        <div className="crewBoardHeader">
          <div className="crewBoardTitle">
            <h3 className="crewBoardTitle">{board.title}</h3>
          </div>
          <div className="crewBoardInfo">
            <div className="crewBoardWriter">
              {/* <label className='crewBoardWriter2'>작성자</label> */}
              <span className="crewBoardWriter2">{board.memberNickName}</span>
            </div>
            <div className="crewBoardTime">
              <span className="crewBoardCreatedTime">│</span>
              <span className="crewBoardCreatedTime">{board.createTime && formattedDate(board.createTime)}</span>
              {board.updateTime &&
              <>
              <div className="crewBoardUpdatedTime">
                <span className='crewBoardUpdatedTime2'>수정</span>
                <span className="crewBoardUpdatedTime2">{formattedDate(board.updateTime)}</span> 
              </div>
              </>
              }
            </div>
          </div>
        </div>
        <div className="crewBoardContent">
          <p className="crewBoardContent2">{board.content}</p>
          <div className="crewBoardImage">
            {board.newFileName && board.newFileName.length > 0 && (
              <div className="crewBoardImages">
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
          </div>
        </div>
      </div>

      <div className="crewBoardComment">
        <div className="crewBoardComment-con">
          <div className="writeComment">
            <h3 className='crewComment'>댓글({totalComments})</h3>
            <div className="writeComment-con">
              <textarea 
                name="comment"
                id='comment'
                value={comment}
                onChange={(e) => setComment(e.target.value)}
                required
                placeholder='타인을 배려하는 마음으로 댓글을 달아주세요.' 
              />
              <button type="button" onClick={submitComment}>등록</button>
            </div>
          </div>
          <div className="commentList">
            {comments.length > 0 ? comments.map((comment) => (
              <div key={comment.id} className="aComment">
                <span className='commentWriter'>{comment.memberNickName}</span>
                <span className="commentTime">{comment.createTime && formattedDateForComment(comment.createTime)}</span>
                <div className="commentContent">
                  <p>{comment.content}</p>
                </div>
                {comment.memberId === loginMemberId && (
                  <div className="commentBtn">
                    {/* <button onClick={}>수정</button> */}
                    <button onClick={() => deleteComment(comment.id, comment.memberId)}>x</button>
                  </div>
                )}
              </div>
            )) : <span>댓글이 없습니다.</span>}

            <div className="commentListPagination">
              {/* <button onClick={() => setPage(0)} disabled={page === 0}>처음</button> */}
              <button onClick={() => setPage(page - 1)} disabled={!hasPrevious}>이전</button>
              {Array.from({length: endPage - startPage + 1}, (_, idx) => (
                <button
                  key={idx}
                  onClick={() => setPage(startPage + idx - 1)}
                  disabled={startPage + idx - 1 === page}
                >
                  {startPage + idx}
                </button>
              ))}
              <button onClick={() => setPage(page + 1)} disabled={!hasNext}>다음</button>
              {/* <button onClick={() => setPage(totalPages - 1)} disabled={page === totalPages - 1}>마지막</button> */}
            </div>
          </div>
        </div>
      </div>
      <div className="crewBoardBtn">
        <div className="crewBoardListBtn">
          <button className='crewBoardBtnBtn' onClick={() => navigate(`/mycrew/${crewId}/board/list`)}>글목록</button>
        </div>
        <div className="crewBoardWrite">
          <button className='crewBoardBtnBtn' onClick={() => navigate(`/mycrew/${crewId}/board/create`)}>글작성</button>
          {board.memberId === loginMemberId && (
            <>
              <button className='crewBoardBtnBtn' onClick={() => navigate(`/mycrew/${crewId}/board/update/${boardId}`)}>수정</button>
              <button className='crewBoardBtnBtn' onClick={deleteBoard}>삭제</button>
            </>
          )}
        </div>
      </div>
    </div>
  )
}

export default MyCrewBoardDetailContainer