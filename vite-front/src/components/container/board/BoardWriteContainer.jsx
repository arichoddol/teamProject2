import axios from 'axios';
import React, { useEffect, useState } from 'react'
import { Navigate, useNavigate } from "react-router-dom";


const BoardWriteContainer = () => {

    // const [data, setData] = useState([]);
  const [boards, setBoards] = useState([]);

  const navigate = useNavigate();

  const handleSubmit = async(e)=>{
    e.preventDefault();

    const formData = new FormData(e.target);
    try{
        await axios.post("http://localhost:8088/api/board/write", formData);
        alert("게시물이 등록되었습니다");
        navigate("/board");
    
    } catch(err) {
        console.error("게시물등록 실패!", err);
        alert("글쓰기 실패");

    }
  };

  const fetchData = async ()=>{
    // this code for BackEnd Controller 
    const response = await axios.get("http://localhost:8088/api/board/newPost");

    // 💡response.data.content에 BoardDto 리스트가 들어있습니다.
    if(response.data && response.data.content){
      // setData(response.data.content);
      setBoards(response.data.content);
    }
  


  };

  useEffect(()=>{
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
     
     {/* plz fix later this blow code for :: Show Currently Login Member Number Or NickName */}
       {/* <input type="text" name="memberId" id="memberId" placeholder="인증된 사용자 ID 입력" readOnly /> */}
                {/* </li> */}
                { /* th:errors 에러 메시지 영역은 이제 서버 응답을 통해 처리되어야 합니다. */}
                <li>
                    <label htmlFor="title">글제목::</label>
                    <input type="text" name="title" id="title" required/>
                </li><br />
                <li>
                    <label htmlFor="content">글내용::</label>
                    <textarea name="content" id="content" rows="10" required></textarea>
                </li>

                <li>
                    <label htmlFor="nickName">NickName::</label>
                    <input type="text" name="nickName" id="nickName" readOnly/>
                </li>
                
                <li>
                    <label htmlFor="boardFile">FILE</label>
                    <input type="file" name="boardFile" id="boardFile"/>
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