import axios from 'axios';
import React, { useEffect, useState } from 'react'
import { Navigate, useNavigate } from "react-router-dom";
import jwtAxios from '../../../apis/util/jwtUtill';
import { useSelector } from 'react-redux';


const BoardWriteContainer = () => {
  const accessToken = useSelector(state => state.jwtSlice.accessToken);
  const memberId = useSelector(state => state.loginSlice.id);
  const nickName = useSelector(state => state.loginSlice.nickName);


  // const [data, setData] = useState([]);
  const [boards, setBoards] = useState([]);

  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();

    // below promise do not change anythings..
    // this section is JWT TEST sEctio n
    const formData = new FormData(e.target);
    try {
      await jwtAxios.post(`http://localhost:8088/api/board/write?memberId=${memberId}`, formData,
        {
          headers: { Authorization: `Bearer ${accessToken}` },
          withCredentials: true
        },
      );
      alert("게시물이 등록되었습니다");
      navigate("/board");

    } catch (err) {
      console.error("게시물등록 실패!", err);
      alert("글쓰기 실패");

    }
  };


  const fetchData = async () => {
    // this code for BackEnd Controller 
    if (accessToken === null || accessToken === "") {
      navigate("/auth/login");
    }
    const response = await jwtAxios.get("http://localhost:8088/api/board/newPost",
      {
        headers: { Authorization: `Bearer ${accessToken}` },
        withCredentials: true
      },
    );

    // 💡response.data.content에 BoardDto 리스트가 들어있습니다.
    if (response.data && response.data.content) {
      // setData(response.data.content);
      setBoards(response.data.content);
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  return (
    <div className="boardPost">
      <h1>:: 글쓰기 ::</h1>
      <div className="boardPost-con">


        {/* JS로 제출하거나 서버의 /board/write 엔드포인트로 데이터를 보냅니다.*/}
        {/* <form action="http://localhost:8088/api/board/write" method="post" encType="multipart/form-data"> */}
        <form onSubmit={handleSubmit} encType="multipart/form-data">
          <h4>:: 게시글작성 ::</h4>
          <ul>
            <li className="first_li">
              <label htmlFor='memberId'>MEMBER_ID::</label>
              <input type="text" name="memberId" id="memberId" readOnly />
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