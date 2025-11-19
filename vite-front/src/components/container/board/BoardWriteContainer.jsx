import axios from 'axios';
import React, { useEffect, useState } from 'react'
import { Navigate, useNavigate, useParams } from "react-router-dom";
import jwtAxios from '../../../apis/util/jwtUtil';
import { useSelector } from 'react-redux';


const BoardWriteContainer = () => {
  const accessToken = useSelector(state => state.jwtSlice.accessToken);
  const memberId = useSelector(state => state.loginSlice.id);
  const nickName = useSelector(state => state.loginSlice.nickName);

  // tmp
    const { id } = useParams();
    const initialBoardState = {
    id: null,
    memberId: memberId,
    title: '',
    content: '',
    memberNickName: nickName,

  };


  const [boards, setBoards] = useState([]);

  const navigate = useNavigate();

  const handleChange = (e) => {
    setBoards({
      ...boards,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const formData = new FormData();
    formData.append('id', boards.id);
    formData.append('memberId', boards.memberId); // 수정 권한 확인을 위해 필요
    formData.append('title', boards.title);
    formData.append('content', boards.content);
    
    const boardFile = e.target.boardFile.files[0];
    if (boardFile) {
      formData.append('boardFile', boardFile);
    }


    // below promise do not change anythings..
    // this section is JWT TEST sEctio n
      if (memberId) {
        formData.append('memberId', memberId);
    }
    
    try {
      await jwtAxios.post(`http://localhost:8088/api/board/write?memberId=${memberId}`, formData,
        {
          headers: { Authorization: `Bearer ${accessToken}` },
          withCredentials: true
        },
      );
      alert(`${boards.id}번 게시물이 등록되었습니다`);
      navigate("/board");

    } catch (error) {
      console.error("게시물등록 실패!", error);
      alert("글쓰기 실패");
      if (error.response && error.response.data) {
        alert("수정 실패: " + error.response.data); // 서버 오류 메시지 출력 ("수정 권한이 없습니다." 등)
      } else {
        alert("게시물 수정 중 알 수 없는 오류가 발생했습니다.");
      }

    }
  };


  const fetchData = async () => {


    // there is NO Token ... Send Login...=>
    if (!accessToken) {
      navigate("/auth/login");
      return;
    }


    try{
    const response = await jwtAxios.get(`${API_BASE_URL}/newPost`,
      {
        headers: { Authorization: `Bearer ${accessToken}` },
        withCredentials: true
      },
    );

    const data = response.data;
        // Set into Data <- Bring Data
      setBoards({
        id: data.id,
        memberId: data.memberId,
        title: data.title,
        content: data.content,
        memberNickName: data.nickName || data.memberNickName || nickName,
      });

            console.log(response.data.content)
    } catch (error) {
      console.error("게시물 조회 실패:", error.response);
 if (error.response) {
        if (error.response.status === 400) {
          alert(error.response.data);
          navigate(`/board/${memberId}`);
        } else if (error.response.status === 404) {
          alert("게시글 정보를 찾을 수 없습니다.");
          navigate("/board");
        } else {
          alert("서버 오류로 게시글 정보를 가져오지 못했습니다.");
        }
      } else {
        alert("네트워크 오류가 발생했습니다.");
      }
    }
  }
  
  useEffect(() => {
    fetchData();
  }, [id, accessToken]);

  return (
    <div className="boardPost">
      <h1>:: 글쓰기 ::</h1>
      <div className="boardPost-con">


       
        <form onSubmit={handleSubmit} encType="multipart/form-data">
          <h4>:: 게시글작성 ::</h4>
          <ul>
            <li className="first_li">
              <label htmlFor='memberId'>MEMBER_ID::</label>
              <input type="text" name="memberId" id="memberId" value={memberId} readOnly />
            </li>

            <li>
              <label htmlFor="title">글제목::</label>
              <input type="text" name="title" id="title" required />
            </li><br />
            <li>
              <label htmlFor="content">글내용::</label>
              <textarea name="content" id="content" rows="10" required></textarea>
            </li>

            <li>
              <label htmlFor="nickName">NickName::</label>
              <input type="text" name="nickName" id="nickName" value={nickName} readOnly />
            </li>

            <li>
              <label htmlFor="boardFile">FILE</label>
              <input type="file" name="boardFile" id="boardFile" />
            </li>

            <li>
              <input type="submit" value="글작성" className="last" />

              <a href="/board/index" className="last">게시글목록</a>
            </li>
          </ul>
        </form>

      </div>
    </div>
  )
}

export default BoardWriteContainer