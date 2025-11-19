import axios from 'axios';
import React, { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import jwtAxios from '../../../apis/util/jwtUtil';
import { useSelector } from 'react-redux';


const BoardUpdateContainer = () => {

  // JWT
  const accessToken = useSelector(state => state.jwtSlice.accessToken);
  const memberId = useSelector(state => state.loginSlice.id);
  const nickName = useSelector(state => state.loginSlice.nickName);


  const initialBoardState = {
    id: null,
    memberId: memberId,
    title: '',
    content: '',
    memberNickName: nickName,

  };

  const [boards, setBoards] = useState(initialBoardState);
  const { id } = useParams();
  const navigate = useNavigate();
  const API_BASE_URL = 'http://localhost:8088/api/board';


  const handleChange = (e) => {
    setBoards({
      ...boards,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    // below promise do not change anythings..
    // this section is JWT TEST sEctio n
    const formData = new FormData();
    formData.append('id', boards.id);
    formData.append('memberId', boards.memberId); // 수정 권한 확인을 위해 필요
    formData.append('title', boards.title);
    formData.append('content', boards.content);

    const boardFile = e.target.boardFile.files[0];
    if (boardFile) {
      formData.append('boardFile', boardFile);
    }

    try {
      await jwtAxios.post(`${API_BASE_URL}/updatePost`, formData,
        {
          headers: { Authorization: `Bearer ${accessToken}` },
          withCredentials: true
        },
      );
      alert(`${boards.id}번 게시물이 수정되었습니다`);
      navigate(`/board/detail/${boards.id}`); // 수정된 게시글 상세 페이지로 이동

    } catch (error) {
      console.error("게시물등록 실패!", error);
      alert("글쓰기 실패");

      if (err.response && err.response.data) {
        alert("수정 실패: " + err.response.data); // 서버 오류 메시지 출력 ("수정 권한이 없습니다." 등)
      } else {
        alert("게시물 수정 중 알 수 없는 오류가 발생했습니다.");
      }
    }
  };

  const handleUpdateSubmit = async (e) => {
    e.preventDefault();
    const formData = new FormData(e.target);
    formData.append('id', boards.id);
    try {
      await jwtAxios.post(`${API_BASE_URL}/updatePost`, formData,
        {
           headers: { Authorization: `Bearer ${accessToken}` },
           withCredentials: true
        },
      );
      alert("게시물이 수정되었습니다");
      navigate("/board");

    } catch (err) {
      console.error("게시물수정 실패!", err);
      alert("글쓰기 실패");
    }
  };

  // Data Fetching
  const fetchData = async () => {

    // there is NO Token ... Send Login...=>
    if (!accessToken) {
      navigate("/auth/login");
      return;
    }

    try {
      // id from parameter
      // GET /api/board/update/{id} Call
      const response = await jwtAxios.get(`${API_BASE_URL}/update/${id}`,
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
          navigate(`/board/${id}`);
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
  };

  useEffect(() => {
    fetchData();

  }, [id, accessToken]);




  return (
    <div className="boardUpdate">
      <h4>:: 게시글수정하기 ::</h4>
      <div className="boardUpdate-con">

        <form onSubmit={handleUpdateSubmit} encType="multipart/form-data">
          <ul>
            <li className="first_li">
              <label htmlFor='memberId'>글 ID::</label>
              {/* boards.id를 표시 */}
              <input type="text" name="id" id="id" value={boards.id || ''} readOnly />
            </li>
            <li>
              <label htmlFor="title">글제목::</label>
              {/* value & onChange Connection */}
              <input
                type="text"
                name="title"
                id="title"
                value={boards.title}
                onChange={handleChange}
                required
              />
            </li><br />
            <li>
              <label htmlFor="content">글내용::</label>
              {/* value & onChange Connection */}
              <textarea
                name="content"
                id="content"
                rows="10"
                value={boards.content}
                onChange={handleChange}
                required
              ></textarea>
            </li>

            <li>
              <label htmlFor="memberNickName">NickName::</label>
              {/* boards.memberNickName를 표시 */}
              <input type="text" name="memberNickName" id="memberNickName" value={boards.memberNickName} readOnly />
            </li>

            <li>
              <label htmlFor="boardFile">FILE (새 파일로 변경)</label>
              <input type="file" name="boardFile" id="boardFile" />
            </li>

            <li>
              <input type="submit" value="글수정" className="last" />
              <a href={`/board/detail/${boards.id}`} className="last">취소/돌아가기</a>
            </li>
          </ul>
        </form>

      </div>
    </div>
  )
}


export default BoardUpdateContainer