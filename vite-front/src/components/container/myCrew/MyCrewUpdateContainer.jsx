import axios from 'axios';
import React, { useEffect, useState } from 'react'
import { useSelector } from 'react-redux';
import { useNavigate, useParams } from 'react-router';
import jwtAxios from '../../../apis/util/jwtUtil';

const MyCrewUpdateContainer = () => {
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
    const [deleteImageName, setDeleteImageName] = useState([])

    useEffect(() => {
      const fetchCrewDetail = async () => {
        try {
          const res = await jwtAxios.get(`/api/crew/detail/${crewId}`,
            {
                headers: { Authorization: `Bearer ${accessToken} `}
            }
          );
          setCrew(res.data.crewDetail);
          setCrewName(res.data.crewDetail.name)
          setDescription(res.data.crewDetail.description)
          setDistrict(res.data.crewDetail.district)
          console.log(res.data.crewDetail);
          if (res.data.crewDetail.newFileName && res.data.crewDetail.newFileName.length > 0) {
            setExFiles(
                res.data.crewDetail.newFileName.map(name => ({
                    newFileName: name
                }))
            );
          };
        } catch (err) {
          console.error("크루 상세 불러오기 실패", err);
        }
      };
      fetchCrewDetail(crewId);
    }, [crewId]);
    
    const deleteImage = (name) => {
        setDeleteImageName(prev => [...prev, name]);
        setExFiles(prev => prev.filter(img => img.newFileName !== name))
    }

    const uploadNewFile = (e) => {
        setNewImages([...e.target.files])
    }

    const update = async (el) => {
        el.preventDefault();
        try {
            const formData = new FormData();
            formData.append("crewName", crewName);
            formData.append('description', description)
            formData.append('district', district)
            newImages.forEach(image => formData.append('newImages', image))
            deleteImageName.forEach(name => formData.append('deleteImageName[]', name));

            const response = await axios.put(`http://localhost:8088/api/mycrew/${crewId}/update`,
                formData,
                {
                    headers: {
                        "Authorization": `Bearer ${accessToken}`
                    },
                }
            );
            alert('크루 수정 완료')
            navigate(`/mycrew/${crewId}`)
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
                        exFiles.map((img, idx) => (
                            <li key={idx}>
                                <img src={img.newFileName} alt={img.newFileName} />
                                <button type='button' onClick={() => deleteImage(img.newFileName)}>X</button>
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
            <div className="crweUpdateBtn">
              <button className='crewUpdate' type='submit'>수정완료</button>
            </div>
            </form>
          </div>
        </div>
    );
}

export default MyCrewUpdateContainer