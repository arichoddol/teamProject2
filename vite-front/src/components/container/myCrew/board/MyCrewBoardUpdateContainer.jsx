import React, { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom';
import { useSelector } from 'react-redux'
import axios from 'axios';

const MyCrewBoardUpdateContainer = () => {
  const { crewId, boardId } = useParams();
  const navigate = useNavigate();
  const { accessToken } = useSelector((state) => state.jwtSlice);
//   const { userEmail } = useSelector((state) => state.loginSlice);  // 닉네임이 슬라이스에 있으면 좋겠당
  const { nickName } = useSelector((state) => state.loginSlice);

  const [board, setBoard] = useState([]);
  const [title, setTitle] = useState('');
  const [content, setContent] = useState('');
  const [newImages, setNewImages] = useState([]);
  const [exFiles, setExFiles] = useState([]);
  const [deleteImageId, setDeleteImageId] = useState([]);

  useEffect(() => {
      const fetchBoard = async () => {
        try {
            const res = await axios.get(`/api/mycrew/${crewId}/board/detail/${boardId}`);
            setBoard(res.data.boardDetail);
            setTitle(res.data.boardDetail.title);
            setContent(res.data.boardDetail.content);
            if (res.data.boardDetail.crewBoardImageEntities && res.data.boardDetail.crewBoardImageEntities.length > 0) {
                setExFiles(
                    res.data.boardDetail.crewBoardImageEntities.map(img => ({
                        id: img.id,
                        newName: img.newName
                    }))
                );
            };
        } catch (err) {
            console.error("게시물 불러오기 실패", err)
        }
      };
      fetchBoard();
  }, [crewId, boardId])

  const deleteImage = (id) => {
    setDeleteImageId(prev => [...prev, id]);
    setExFiles(prev => prev.filter(img => img.id !== id));
  }

  const uploadNewFile = (e) => {
    setNewImages([...e.target.files])
  };

  const update = async (el) => {

    el.preventDefault();
    const formData = new FormData();
  formData.append("title", title);
  formData.append("content", content);

  // 새 이미지 첨부
  for (let i = 0; i < newImages.length; i++) {
    formData.append("newImages", newImages[i]);
  }

  // 삭제할 이미지 ID
  deleteImageId.forEach(id => formData.append("deleteImageId", id));

  try {
    const res = await axios.post(
      `/api/mycrew/${crewId}/board/update/${boardId}`,
      formData,
      {
        headers: {
          Authorization: `Bearer ${accessToken}`,
          "Content-Type": "multipart/form-data"
        }
      }
    );

    console.log("업데이트 완료", res.data.updatedBoard);
    alert("게시글이 수정되었습니다!");
    navigate(`/mycrew/${crewId}/board/detail/${boardId}`);
  } catch (err) {
    console.error("게시글 수정 실패", err);
    alert("게시글 수정 실패: " + err.response?.data?.error);
  }
};

  return (
    <div className="boardCreate">
        <div className="boardCreate-con">
            <form onSubmit={update}>
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
                        <span>기존 이미지</span>
                        <ul>
                            {exFiles.length > 0 ? (
                                exFiles.map(img => (
                                <li key={img.id}>
                                    <img src={img.newName} alt="boardImage" />
                                    <button type='button' onClick={() => deleteImage(img.id)}>X</button>
                                </li>
                            ))
                        ) : (
                            <p>이미지 없음</p>
                        )}
                        </ul>
                        <span>새 이미지 업로드</span>
                        <input type="file" name='crewBoardImages' onChange={uploadNewFile} multiple/>
                    </div>
                    <div className="boardCreater">
                        <label className="memberNickName">작성자</label>
                        <input type="text" value={nickName} readOnly/>
                    </div>
                    <button className="boardUpdate" type="submit">수정완료</button>
                </div>
            </form>
        </div>
    </div>
  )
}

export default MyCrewBoardUpdateContainer