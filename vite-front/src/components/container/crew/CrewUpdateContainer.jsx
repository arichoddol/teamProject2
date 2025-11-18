import axios from 'axios';
import React, { useEffect, useState } from 'react'
import { useSelector } from 'react-redux';
import { useNavigate, useParams } from 'react-router';

const CrewUpdateContainer = () => {
    const { crewId } = useParams();
    const navigate = useNavigate();
    const { accessToken } = useSelector((state) => state.jwtSlice);
    const loginMemberId = useSelector(state => state.loginSlice.id)
    
    const [crew, setCrew] = useState({})
    const [crewName, setCrewName] = useState('')
    const [description, setDescription] = useState('')
    const [district, setDistrict] = useState('')
    const [newImages, setNewImages] = useState([])
    const [exFiles, setExFiles] = useState([])
    const [deleteImageId, setDeleteImageId] = useState([])

    useEffect(() => {
      const fetchCrewDetail = async (crewId) => {
        try {
          const res = await axios.get(`/api/crew/detail/${crewId}`,
            {
                headers: { Authorization: `Bearer ${accessToken} `}
            }
          );
          setCrew(res.data.crewDetail);
          setCrewName(res.data.crewDetail.name)
          setDescription(res.data.crewDetail.description)
          setDistrict(res.data.crewDetail.district)
          console.log(res.data.crewDetail);
          if (res.data.crewDetail.crewImageEntities && res.data.crewDetail.crewImageEntities.length > 0) {
            setExFiles(
                res.data.crewDetail.crewImageEntities.map(img => ({
                    id: img.id,
                    newName: img.newName
                }))
            );
          };
        } catch (err) {
          console.error("크루 상세 불러오기 실패", err);
        }
      };
      fetchCrewDetail(crewId);
    }, [crewId]);
    
    const deleteImage = (id) => {
        setDeleteImageId(prev => [...prev, id]);
        setExFiles(prev => prev.filter(img => img.id !== id))
    }

    const uploadNewFile = (e) => {
        setNewImages([...e.target.files])
    }

    const update = async (el) => {
        el.preventDefault();
        try {
            const formData = new FormData();
            formData.append("name", crewName);
            formData.append('description', description)
            formData.append('district', district)
            newImages.forEach(image => formData.append('newImages', image))
            deleteImageId.forEach(id => formData.append('deleteImageId', id));

            const response = await axios.put(`/api/crew/update/${crewId}`,
                formData,
                {
                    headers: {
                        "Authorization": `Bearer ${accessToken}`
                    },
                }
            );
            alert('크루 수정 완료')
            navigate(`/crew/detail/${crewId}`)
        } catch (err) {
            console.log(err);
            alert('크루 수정 실패')
        }
    }

    return (
        <div className="crewDetailHome">
          <div className="crewDetailHome-con">
            <form onSubmit={update}>
            <div className="image">
                <span>기존 이미지</span>
                <ul>
                    {exFiles.length > 0 ? (
                        exFiles.map(img => (
                            <li key={img.id}>
                                <img src={img.newName} alt="crewImage" />
                                <button type='button' onClick={() => deleteImage(img.id)}>X</button>
                            </li>
                        ))
                    ) : (
                        <div>이미지 없음</div>
                    )}                
                </ul>
                <span>새 이미지 업로드</span>
                <input type="file" name='crewBoardImgaes' onChange={uploadNewFile} multiple />
            </div>
            <div className="introduction">
              <h2>
                <input 
                    type="text"
                    value={crewName}
                    onChange={(e) => setCrewName(e.target.value)}
                    required
                />
              </h2>
              <p>
                <textarea
                    name="description" 
                    id="description"
                    value={description}
                    onChange={(e) => setDescription(e.target.value)}
                />
              </p>
              <p>
                <input 
                    type="text"
                    value={district}
                    onChange={(e) => setDistrict(e.target.value)}
                    required 
                />
              </p>
              <p>리더 {crew.memberNickName}</p>
              <p>멤버 {crew.crewMemberEntities?.length ?? 0}명</p>
            </div>
            </form>
          </div>
        </div>
    );
}

export default CrewUpdateContainer