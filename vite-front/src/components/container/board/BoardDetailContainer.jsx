import axios from 'axios';
import React, { useEffect, useState } from 'react'
import { useSelector } from 'react-redux';
import { useNavigate, useParams } from 'react-router-dom'



const BoardDetailContainer = () => {

    const memberId = useSelector(state => state.loginSlice.id);

    const [boards, setBoards] = useState([]);
    const {id} = useParams();
    const navigate = useNavigate();

    const API_BASE_URL = 'http://localhost:8088/api/board';
    const IMAGE_BASE_URL = 'http://localhost:8088/upload/';
    //  private static final String FILE_PATH = "C:/full/upload/";

    const fetchData = async ()=>{
      const response = await axios.get(`http://localhost:8088/api/board/detail/${id}`);

    // 🚨 수정: 상세 조회는 일반적으로 단일 객체(BoardDto)가 response.data에 바로 담겨 옵니다.
    // Paging 처리가 된 List 형태가 아니므로 .content 확인 로직을 제거하고,
    // response.data가 유효할 경우 바로 setBoard에 할당합니다.
    // 💡response.data.content에 BoardDto 리스트가 들어있습니다.
    if(response.data){
      setBoards(response.data);
    } else {
      console.log("게시물 데이터가 존재하지 않음.")
    }
  
    // 백엔드: @GetMapping("/detail/{id}") =>
    // 프론트엔드: `.../api/board/detail/${id}`
  
  };


    useEffect(()=>{
      if(id) {
        fetchData();
      } 
  }, [id]);   //=> this Arguemnt run changeId


    const handleDelete = async () => {
        if (!window.confirm('정말로 이 게시글을 삭제하시겠습니까?')) {
            return; // 사용자가 취소
        }

        try {
            const response = await fetch(`${API_BASE_URL}/detail/${boards.id}`, {
                method: 'DELETE', // DELETE 요청 전송
            });

            if (response.ok) {
                alert('게시글이 성공적으로 삭제되었습니다.'); // 사용자에게 알림
                navigate('/board/index'); // 삭제 후 게시판 목록으로 이동
            } else if (response.status === 404) {
                alert('삭제할 게시글을 찾을 수 없습니다.');
            } else {
                const errorText = await response.text();
                throw new Error(`삭제 실패: ${errorText}`);
            }
        } catch (error) {
            console.error('게시글 삭제 중 오류 발생:', error);
            alert('게시글 삭제 중 오류가 발생했습니다. 잠시 후 다시 시도해 주세요.');
        }
    };

    const handleUpdatePost = (boardId) =>{
      navigate(`/board/update/${boardId}`);

    }

 







  return (
    <div className="boardDetail">
        <h4>{boards.title}</h4>
        <div className="boardDetail-con-info">
          <span>작성자 : {boards.memberNickName} </span>
          <span>조회수 : {boards.hit} </span>
          <span>작성일 : {boards.createTime} </span>
        </div>
        <div className="boardDetail-con">
          <p>{boards.content}<br /></p>
            <div className="boardDetail-con-image">
              {console.log(boards)}
              { boards.boardImgDtos && boards.boardImgDtos.length > 0 && (
                  boards.boardImgDtos.map((imgDto)=>(
                  <img 
                       // bring File by NewName Field
                        key={imgDto.id || imgDto.newName} 
                        src={`${IMAGE_BASE_URL}${imgDto.newName}`} 
                        alt={imgDto.oldName}
                        style={{ maxWidth: '100%', height: 'auto', display: 'block', margin: '10px 0' }}
                    />

                  ))
              )}
              {/* 이미지가 하나만 첨부되었더라도, .map()을 사용하여 로직을 유지하는 것이 표준 방식 */}
              
              <div className="boardDetail-con-tag">
              </div>
            </div>
            <div className="boardDetail-act">
              <button onClick={()=> handleUpdatePost(boards.id)}>게시글 수정</button>
              <button onClick={handleDelete}>게시글 삭제</button>
              
            </div>
        </div>
    </div>
  )
}

export default BoardDetailContainer