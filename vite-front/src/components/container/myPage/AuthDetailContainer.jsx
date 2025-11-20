import React, { useEffect, useState } from "react";
import { authDetailFn } from "../../../apis/auth/authDetail";
import { useSelector } from "react-redux";
import { BACK_BASIC_URL } from "../../../apis/commonApis";
import { useNavigate } from "react-router";
import jwtAxios from "../../../apis/util/jwtUtil";

const AuthDetailContainer = () => {
  const navigate = useNavigate();
  const accessToken = useSelector((state) => state.jwtSlice.accessToken);
  const [memberDetail, setMemberDetail] = useState({});
  const [gender, setGender] = useState("남성");

  useEffect(() => {
    if (accessToken == null) {
      alert("로그인 후 접속해주세요.");
      navigate("/auth/login");
    }
    const myPageDetailFn = async () => {
      const res = await authDetailFn();
      const authDetail = res.data;
      console.log(authDetail);

      if (res.status === 200) {
        setMemberDetail({ ...authDetail });
        if (authDetail.gender === "WOMAN") {
          setGender("여성");
        }
      }
    };
    myPageDetailFn();
  }, []);

  const memberDelefeFn = async () => {
    const rs = window.confirm("정말 탈퇴하시겠습니까?");
    if (rs) {
      try {
        await jwtAxios.delete(
          `${BACK_BASIC_URL}/api/member/delete/${memberDetail.id}`,
          { headers: { Authorization: `Bearer ${accessToken}` } }
        );

        await axios.post(
          `${BACK_BASIC_URL}/api/member/logout`,
          {},
          {
            headers: {
              Authorization: `Bearer ${accessToken}`,
            },
            withCredentials: true,
          }
        );

        localStorage.removeItem(ACCESS_TOKEN_KEY);
        alert("탈퇴 완료되었습니다. 안녕히가세요.");
        navigate("/store/index");
      } catch (err) {
        console.log("탈퇴 실패 " + err);
      }
    }
  };

  const updatePage = () => {
    navigate("/auth/myPage/update");
  };

  return (
    <main className="memberDetail-right">
      <div className="profile-section">
        {memberDetail.profileImagesList != null ? (
          <div className="profile-image">
            <span className="profile-label">프로필 이미지</span>
            <img
              src={`${BACK_BASIC_URL}/upload/${memberDetail.profileImagesList[0].newName}`}
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
      </div>

      <div className="info-section">
        <div className="info-item">
          <span className="info-label">이메일</span>
          <span className="info-value">{memberDetail.userEmail}</span>
        </div>
        <div className="info-item">
          <span className="info-label">이름</span>
          <span className="info-value">{memberDetail.userName}</span>
        </div>
        <div className="info-item">
          <span className="info-label">닉네임</span>
          <span className="info-value">{memberDetail.nickName}</span>
        </div>
        <div className="info-item">
          <span className="info-label">주소</span>
          <span className="info-value">{memberDetail.address}</span>
        </div>
        <div className="info-item">
          <span className="info-label">전화번호</span>
          <span className="info-value">{memberDetail.phone}</span>
        </div>
        <div className="info-item">
          <span className="info-label">나이</span>
          <span className="info-value">{memberDetail.age}세</span>
        </div>
        <div className="info-item">
          <span className="info-label">성별</span>
          <span className="info-value">{gender}</span>
        </div>
      </div>

      <div className="button-group">
        <button className="btn-edit" onClick={updatePage}>
          수정
        </button>
        <button className="btn-delete">탈퇴</button>
      </div>
    </main>
  );
};

export default AuthDetailContainer;
