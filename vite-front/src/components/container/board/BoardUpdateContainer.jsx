import axios from 'axios';
import React, { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'

const BoardUpdateContainer = () => {

    const initialBoardState = {
    id: null,
    memberId: 1, 
    title: '',
    content: '',
    memberNickName: '' 
    };

    const [boards, setBoards] = useState(initialBoardState);
    const {id} = useParams();
    const navigate = useNavigate();
    const API_BASE_URL = 'http://localhost:8088/api/board';



    const handleUpdateSubmit = async(e) => {
      e.preventDefault();
      const formData = new FormData(e.target);
      formData.append('id', boards.id);
      try{
        await axios.post("http://localhost:8088/api/board/updatePost", formData);
        alert("게시물이 수정되었습니다");
        navigate("/board");
    
    } catch(err) {
        console.error("게시물수정 실패!", err);
        alert("글쓰기 실패");
    }
  };

  // Data Fetching
    const fetchData = async ()=>{

    const response = await axios.get(`${API_BASE_URL}/update/${id}`);
    const data = response.data;

    console.log(response.data.content

    )

    if(data) {
      setBoards({
                    id: data.id,
                    memberId: data.memberId || 1, 
                    title: data.title || '',
                    content: data.content || '',
                    memberNickName: data.memberNickName || '',
                });
    } else {
                console.log("게시물 데이터가 존재하지 않거나 형식이 올바르지 않습니다.");
                alert("게시글 정보를 찾을 수 없습니다.");
                // navigate("/board/index");
           }
    };

  useEffect(()=>{
    fetchData();
     
  }, []);




  return (
    <div className="boardUpdate">
      <h4>:: 게시글수정하기 ::</h4>
      <div className="boardUpdate-con">
           <form onSubmit={handleUpdateSubmit} encType="multipart/form-data">
        { console.log(boards) }
          <ul>
            <li className="first_li">
                <label htmlFor='memberId'>글 ID::</label> 
                 <input type="text" name="memberId" id="memberId"  value={boards.id || ''}  readOnly />   
                </li> 
                <li>
                    <label htmlFor="title">글제목::</label>
                    <input type="text" name="title" id="title" defaultValue={boards.title} required></input>
                </li><br />
                <li>
                    <label htmlFor="content">글내용::</label>
                    <textarea name="content" id="content" rows="10" defaultValue={boards.content} required></textarea>
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
                    <input type="submit" value="글수정" className="last" />

                    <a href="/board/index" className="last">게시글목록</a>
                </li>
            </ul>
        </form>

      </div>
    </div>
  )
}


export default BoardUpdateContainer