import React, { useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom';
import { useSelector } from 'react-redux'
import axios from 'axios';

const MyCrewBoardCreateContainer = () => {
  const { crewId } = useParams();
  const navigate = useNavigate();
  const { accessToken } = useSelector((state) => state.jwtSlice);
  const { userEmail, nickName } = useSelector((state) => state.loginSlice);  // 닉네임이 슬라이스에 있으면 좋겠당

  const [title, setTitle] = useState('');
  const [content, setContent] = useState('');
  const [files, setFiles] = useState([]);

  const fileChange = (e) => {
    const selectedFiles = Array.from(e.target.files)
    setFiles(exFiles => [...exFiles, ...selectedFiles])
  }

  const removeFile = (idx) => {
    setFiles(exFiles => exFiles.filter((_, i) => i != idx))
  }

  const create = async (el) => {
    el.preventDefault();
    console.log('accessToken:', accessToken);

    try {
        const formData = new FormData();
        formData.append('title', title);
        formData.append('content', content);

        if (files.length > 0) {
            // 선택된 파일이 있을 경우 모두 추가
            files.forEach(file => formData.append('crewBoardFile', file));
        } else {
            // 파일이 없으면 빈 파일을 하나 넣어서 multipart 형식 유지
            const emptyFile = new File([''], 'empty.txt', { type: 'text/plain' });
            formData.append('crewBoardFile', emptyFile);
        }

        const response = await axios.post(
            `/api/mycrew/${crewId}/board/create`,
            formData,
            {
                headers: {
                    "Authorization": `Bearer ${accessToken}`,
                    "Content-Type": "multipart/form-data"
                },
                withCredentials: true, // 쿠키 사용 시 필요
            }
        );

        alert('게시글 작성 완료');
        navigate(`/mycrew/${crewId}/board/detail/${response.data.id}`);
    } catch (err) {
        console.error(err.response || err);
        alert("오류 발생: " + (err.response?.data?.error || err.message));
    }
};

  return (
    <div className="boardCreate">
        <div className="boardCreate-con">
            <form onSubmit={create}>
                <div className="boardTitle">
                    <label className="crewBoardLabel">제목</label>
                    <input 
                        type="text"
                        value={title}
                        onChange={(e) => setTitle(e.target.value)}
                        required
                        placeholder='제목'
                    />
                </div>
                <div className="boardContent">
                    <label className="crewBoardLabel">내용</label>
                    <textarea 
                        name="content" 
                        id="content"
                        value={content}
                        onChange={(e) => setContent(e.target.value)}
                        required
                        placeholder='내용'
                    />
                </div>
                <div className="boardFile">
                    <span>파일</span>
                    <input type="file" name='crewBoardFile' onChange={fileChange} multiple/>
                    {files.length > 0 && (
                        <ul>
                            {files.map((file, idx) => (
                                <li key={idx}>
                                    {file.name}
                                    <button type='button' onClick={() => removeFile(idx)}>x</button>
                                </li>
                            ))}
                        </ul>
                    )}
                </div>
                <div className="boardCreater">
                    <label className="crewBoardLabel">작성자</label>
                    <input type="text" value={nickName} readOnly/>
                </div>
                <div className="crewBoardCreateBtn">
                    <button className="crewBoardCreate" type="submit">작성완료</button>
                </div>
            </form>
        </div>
    </div>
  )
}

export default MyCrewBoardCreateContainer