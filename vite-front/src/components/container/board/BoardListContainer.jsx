import axios from 'axios';
import React, { useEffect, useState } from 'react'
import { Link } from 'react-router-dom';


const BoardListContainer = () => {

  // const [data, setData] = useState([]);
  const [boards, setBoards] = useState([]);

  const fetchData = async ()=>{
    // this code for BackEnd Controller 
    const response = await axios.get("http://localhost:8088/api/board");

    // 💡response.data.content에 BoardDto 리스트가 들어있습니다.
    if(response.data && response.data.content){
      // setData(response.data.content);
      setBoards(response.data.content);
    }
    // 배열이 아닌 객체가 할당되어 에러가 난것
    // setBoards(response.data);
  
  };

  useEffect(()=>{
    fetchData();
  }, []);


  // return

  return (
    <div className="boardList">

      <h3>this section for HEADER ::</h3>
      <h3>this section for HEADER ::</h3>
      <h3>this section for HEADER ::</h3>
      <h3>this section for HEADER ::</h3>
      
      <div className="boardList-con">
        <Link to="/board/newPost">
           <h3>글쓰기</h3>
        </Link>
       

        <br /><br /><br />
        <h2>:: 자유게시판 ::</h2>
        <table className='board-table'> 
          <thead>
            <tr>
            <th scope='col'>ID</th>
            <th scope='col'>:: 글제목</th>
            <th scope='col'>:: 작성자</th>
            <th scope='col'>:: 조회수</th>
            <th scope='col'>:: 파일</th>
          </tr>
          </thead>
          <tbody>
            { console.log(boards) }
            { console.log(boards) }

             { boards.map(list =>(
              <tr key={list.id}>
                <td>{list.id}</td>
                <td>{list.title}</td>
                <td>{list.memberNickName}</td>
                <td>{list.hit}</td>
                <td>{list.attachFile}</td>
              </tr>
             ))}
          </tbody>
        </table>
        
      </div>
    </div>
  )
}

export default BoardListContainer