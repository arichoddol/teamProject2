import React, { useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom';
import { useSelector } from 'react-redux'

const MyCrewBoardCreate = () => {
  const { crewId } = useParams();
  const navigate = useNavigate();
  const { accessToken } = useSelector((state) => state.jwtSlice);
 
  const [title, setTitle] = useState('');
  const [content, setContent] = useState('');
  const [memberNickName, setMemberNickName] = useState('');

  const create = async (el) => {
    el.preventDefault();

    try {
        const response = await fetch("/api/mycrew/${crewId}/board/create", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                // "Authorization": `Bearer ${accessToken}`
            },
            body: JSON.stringify({
                title,
                content,
                memberNickName,
            })
        })
    } catch (err) {
        console.log(err);
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
                    <div className="boardCreater">
                        <label className="memberNickName">작성자</label>
                        <input type="text" value={memberNickName} readOnly/>
                    </div>
                    <button className="boardCreate" type="submit">작성완료</button>
                </div>
            </form>
        </div>
    </div>
  )
}

export default MyCrewBoardCreate