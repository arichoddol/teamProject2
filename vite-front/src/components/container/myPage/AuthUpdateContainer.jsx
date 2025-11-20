import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router";
import { authDetailFn } from "../../../apis/auth/authDetail";
import { BACK_BASIC_URL } from "../../../apis/commonApis";
import jwtAxios from "../../../apis/util/jwtUtil";
import { useSelector } from "react-redux";

const AuthUpdateContainer = () => {
  const accessToken = useSelector((state) => state.jwtSlice.accessToken);
  const [gender, setGender] = useState("남성");
  const [imgFile, setImgFile] = useState(null);
  const navigate = useNavigate();
  const [memberDto, setMemberDto] = useState({
    id: "",
    userEmail: "",
    userPassword: "",
    userName: "",
    nickName: "",
    gender: "",
    age: "",
    phone: "",
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setMemberDto((el) => ({ ...el, [name]: value }));
  };

  useEffect(() => {
    const myPageDetailFn = async () => {
      const res = await authDetailFn();
      const authDetail = res.data;
      console.log(authDetail);
      if (res.status === 200) {
        setMemberDto({ ...authDetail });
        if (authDetail.gender === "WOMAN") {
          setGender("여성");
        }
      }
    };
    myPageDetailFn();
  }, []);

  const memberUpdateFn = async (e) => {
    e.preventDefault();
    const formData = new FormData();
    formData.append(
      "memberDto",
      new Blob([JSON.stringify(memberDto)], { type: "application/json" })
    );

    formData.append("memberFile", imgFile);

    try {
      await jwtAxios.put(
        `${BACK_BASIC_URL}/api/member/update/${memberDto.id}`,
        formData,
        {
          headers: {
            Authorization: `Bearer ${accessToken}`,
            "Content-Type": "multipart/form-data",
          },
        }
      );
      alert("수정이 완료되었습니다.");
      navigate("/auth/myPage");
    } catch (err) {
      console.log("업데이트 오류: " + err);
    }
  };

  return (
    <div className="memberDetail">
      <h2>마이페이지</h2>
      <div className="memberDetail-con">
        <aside className="memberDetail-left">
          <nav>
            <ul>
              <li className="active">회원정보</li>
              <li>결제정보</li>
            </ul>
          </nav>
        </aside>
        <form onSubmit={memberUpdateFn}>
          <main className="memberDetail-right">
            <div className="profile-section">
              {memberDto.profileImagesList != null ? (
                <div className="profile-image">
                  <span className="profile-label">프로필 이미지</span>
                  <img
                    src={`${BACK_BASIC_URL}/upload/${memberDto.profileImagesList[0].newName}`}
                    alt="프로필 이미지"
                    onError={(e) => {
                      e.target.src =
                        "https://dummyimage.com/150x150/cccccc/000000&text=No+Image";
                    }}
                  />
                </div>
              ) : (
                <div className="profile-image">
                  <span className="profile-label">프로필 이미지</span>
                  <img
                    src="https://dummyimage.com/150x150/cccccc/000000&text=No+Image"
                    alt="기본 프로필"
                  />
                </div>
              )}
              <input
                type="file"
                onChange={(e) => setImgFile(e.target.files[0])}
              />
            </div>
            <div className="info-section">
              <div className="info-item">
                <span className="info-label">이메일</span>
                <span className="info-value">{memberDto.userEmail}</span>
              </div>
              <div className="info-item">
                <span className="info-label">이름</span>
                <input
                  type="text"
                  name="userName"
                  value={memberDto.userName}
                  onChange={handleChange}
                />
              </div>
              <div className="info-item">
                <span className="info-label">비밀번호</span>
                <input
                  type="text"
                  name="userPassword"
                  onChange={handleChange}
                />
              </div>
              <div className="info-item">
                <span className="info-label">닉네임</span>
                <input
                  type="text"
                  name="nickName"
                  value={memberDto.nickName}
                  onChange={handleChange}
                />
              </div>
              <div className="info-item">
                <span className="info-label">주소</span>
                <input
                  type="text"
                  name="address"
                  value={memberDto.address}
                  onChange={handleChange}
                />
              </div>
              <div className="info-item">
                <span className="info-label">전화번호</span>
                <input
                  type="text"
                  name="phone"
                  value={memberDto.phone}
                  onChange={handleChange}
                />
              </div>
              <div className="info-item">
                <span className="info-label">나이</span>
                <input
                  type="text"
                  name="age"
                  value={memberDto.age}
                  onChange={handleChange}
                />
              </div>
              <div className="info-item">
                <span className="info-label">성별</span>
                <span className="info-value">{gender}</span>
              </div>
            </div>

            <div className="button-group">
              <button className="btn-edit" type="submit">
                수정하기
              </button>
            </div>
          </main>
        </form>
      </div>
    </div>
  );
};

export default AuthUpdateContainer;
