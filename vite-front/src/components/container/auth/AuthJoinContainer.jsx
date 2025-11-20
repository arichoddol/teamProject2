import axios from "axios";
import React, { useState } from "react";
import { BACK_BASIC_URL } from "../../../apis/commonApis";
import { useNavigate } from "react-router";

const AuthJoinContainer = () => {
  const navigate = useNavigate();
  const [errors, setErrors] = useState({});
  const [imgFile, setImgFile] = useState(null);
  const [memberDto, setMemberDto] = useState({
    userEmail: "",
    userPassword: "",
    userName: "",
    nickName: "",
    gender: "MAN",
    age: "",
    phone: "",
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setMemberDto((el) => ({ ...el, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setErrors({});

    const formData = new FormData();

    formData.append(
      "memberDto",
      new Blob([JSON.stringify(memberDto)], { type: "application/json" })
    );

    formData.append("memberFile", imgFile);

    let res;

    try {
      res = await axios.post(`${BACK_BASIC_URL}/api/member/join`, formData, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      });

      alert("회원가입 완료!, 로그인 페이지로 이동합니다.");
      navigate("/auth/login");
    } catch (err) {
      setErrors(err.response.data);
      console.log("회원가입 실패");
    }
  };

  // "Content-Type": "multipart/form-data"
  return (
    <div className="member-join">
      <div className="member-join-con">
        <h2>회원가입</h2>
        <form onSubmit={handleSubmit}>
          <ul>
            <li>
              <label htmlFor="imgFile">프로필 이미지</label>
              <input
                type="file"
                onChange={(e) => setImgFile(e.target.files[0])}
              />
            </li>
            <li>
              <label htmlFor="userEmail">이메일</label>
              <input
                type="email"
                name="userEmail"
                id="userEmail"
                placeholder="이메일"
                onChange={handleChange}
              />
              {errors.userEmail && (
                <p style={{ color: "red", fontSize: "12px" }}>
                  {errors.userEmail}
                </p>
              )}
            </li>
            <li>
              <label htmlFor="userPassword">비밀번호</label>
              <input
                type="password"
                name="userPassword"
                id="userPassword"
                placeholder="비밀번호 4자리 이상 입력해주세요."
                onChange={handleChange}
              />
              {errors.userPassword && (
                <p style={{ color: "red", fontSize: "12px" }}>
                  {errors.userPassword}
                </p>
              )}
            </li>
            <li>
              <label htmlFor="userName">이름</label>
              <input
                type="text"
                name="userName"
                id="userName"
                placeholder="이름"
                onChange={handleChange}
              />
              {errors.userName && (
                <p style={{ color: "red", fontSize: "12px" }}>
                  {errors.userName}
                </p>
              )}
            </li>
            <li>
              <label htmlFor="nickName">닉네임</label>
              <input
                type="text"
                name="nickName"
                id="nickName"
                placeholder="닉네임"
                onChange={handleChange}
              />
              {errors.nickName && (
                <p style={{ color: "red", fontSize: "12px" }}>
                  {errors.nickName}
                </p>
              )}
            </li>
            <li className="member-gender">
              <label htmlFor="gender" className="genderLabel">
                성별
              </label>
              <div className="member-gender-list">
                남자{" "}
                <input
                  type="radio"
                  name="gender"
                  id="MAN"
                  value="MAN"
                  onChange={handleChange}
                  checked
                />{" "}
                여자{" "}
                <input
                  type="radio"
                  name="gender"
                  id="WOMAN"
                  value="WOMAN"
                  onChange={handleChange}
                />
              </div>
            </li>
            <li>
              <label htmlFor="address">주소</label>
              <input
                type="text"
                name="address"
                id="address"
                placeholder="주소"
                onChange={handleChange}
              />
              {errors.address && (
                <p style={{ color: "red", fontSize: "12px" }}>
                  {errors.address}
                </p>
              )}
            </li>
            <li>
              <label htmlFor="age">나이</label>
              <input
                type="number"
                name="age"
                id="age"
                placeholder="나이"
                onChange={handleChange}
              />
              {errors.age && (
                <p style={{ color: "red", fontSize: "12px" }}>{errors.age}</p>
              )}
            </li>
            <li>
              <label htmlFor="phone">전화번호</label>
              <input
                type="text"
                name="phone"
                id="phone"
                placeholder="전화번호"
                onChange={handleChange}
              />
              {errors.phone && (
                <p style={{ color: "red", fontSize: "12px" }}>{errors.phone}</p>
              )}
            </li>
            <li>
              <button type="submit">회원가입</button>
            </li>
          </ul>
        </form>
      </div>
    </div>
  );
};

export default AuthJoinContainer;
