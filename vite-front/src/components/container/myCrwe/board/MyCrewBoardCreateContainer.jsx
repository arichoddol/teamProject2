import React, { useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom';
import { useSelector } from 'react-redux'
import axios from 'axios';

const MyCrewBoardCreateContainer = () => {
  const { crewId } = useParams();
  const navigate = useNavigate();
  const { accessToken } = useSelector((state) => state.jwtSlice);
  const { userEmail } = useSelector((state) => state.loginSlice);  // 닉네임이 슬라이스에 있으면 좋겠당
  //   const { nickName } = useSelector((state) => state.loginSlice);

  const [title, setTitle] = useState('');
  const [content, setContent] = useState('');
  const [files, setFiles] = useState([]);

  const fileChange = (e) => {
    setFiles(e.target.files);
  }

  const create = async (el) => {
    console.log('accessToken:', accessToken);
    el.preventDefault();

    try {
        const formData = new FormData();
        formData.append('title', title);
        formData.append('content', content);

        for (let i = 0; i < files.length; i ++) {
            formData.append('crewBoardFile', files[i]);
        }
        const response = await axios.post(`/api/mycrew/${crewId}/board/create`, 
            formData,
            {
                headers: {
                    "Authorization": `Bearer ${accessToken}`,
                    "Content-Type": "multipart/form-data"
                },                
            }
        );
        alert('게시글 작성 완료');
        navigate(`/mycrew/${crewId}/board/detail/${response.data.id}`);
        } catch (err) {
        console.log(err);
        alert("오류 발생: " + err.message);
    }
  }

  return (
    <div className="boardCreate">
        <div className="boardCreate-con">
            <form onSubmit={create}>
                <div className="boardTitle">
                    <label className="title">제목</label>
                    <input 
                        type="text"
                        value={title}
                        onChange={(e) => setTitle(e.target.value)}
                        required
                        placeholder='제목'
                    />
                    <div className="boardContent">
                        <label className="content">내용</label>
                        <textarea 
                            name="content" 
                            id="content"
                            value={content}
                            onChange={(el) => setContent(el.target.value)}
                            required
                            placeholder='내용'
                        />
                    </div>
                    <div className="boardFile">
                        <span>파일</span>
                        <input type="file" name='crewBoardfile' onChange={fileChange} multiple/>
                    </div>
                    <div className="boardCreater">
                        <label className="memberNickName">작성자</label>
                        <input type="text" value={userEmail} readOnly/>
                    </div>
                    <button className="boardCreate" type="submit">작성완료</button>
                </div>
            </form>
        </div>
    </div>
  )
}

export default MyCrewBoardCreateContainer