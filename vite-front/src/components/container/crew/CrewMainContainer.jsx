import axios from "axios";
import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";

import jwtAxios from "../../../apis/util/jwtUtil";
import { useSelector } from "react-redux";

import "../../../css/crew/crewMain.css";

const CrewMainContainer = () => {
  const accessToken = useSelector((state) => state.jwtSlice.accessToken);
  const [crewList, setCrewList] = useState([]);
  const [myCrewList, setMyCrewList] = useState([]);

  useEffect(() => {
    if (accessToken == null) {
      setMyCrewList([]);
    }
  }, []);

  useEffect(() => {
    axios
      .get(`/api/crew/list`)
      .then((res) => {
        setCrewList(res.data.crewList);
        console.log(res.data.crewList);
      })
      .catch((err) => {
        console.error("크루 목록 실패", err);
      });
  }, []);

  // 본인이 가입한 크루 조회
  useEffect(() => {
    jwtAxios
      .get(`/api/mycrew/list`, {
        headers: { Authorization: `Bearer ${accessToken}` },
        withCredentials: true,
      })
      .then((res) => {
        setMyCrewList(res.data.myCrewList);
        console.log(res.data.myCrewList);
      })
      .catch((err) => {
        console.error("내 크루 목록 조회 실패 ", err);
      });
  }, []);

  return (
    <>
      <div className="crewList">
        <div className="kakaomap">{/* insert here kakaoMapAPI */}</div>
        <div className="crewList-con">
          {accessToken != null ? (
            <>
              <h1>내 크루 목록</h1>
              <ul>
                {myCrewList.map((crew) => {
                  const images = crew.crewImages || [];
                  return (
                    <li key={crew.id}>
                      <Link to={`/mycrew/${crew.crewId}`}>
                        <div className="crewListLeft">
                          {images.length > 0 ? (
                            <img
                              src={images[0]}
                              alt={`${crew.crewName} 이미지`}
                              className="crewImage"
                            />
                          ) : (
                            <div>이미지 없음</div>
                          )}
                        </div>

                        <div className="crewListRight">
                          <h2>{crew.crewName}</h2>
                          <p>{crew.description}</p>
                          <p>{crew.district}</p>
                        </div>
                      </Link>
                    </li>
                  );
                })}
              </ul>{" "}
            </>
          ) : null}
          <h1>크루 목록</h1>
          <ul>
            {crewList.map((crew) => {
              const images = crew.newFileName || [];
              return (
                <li key={crew.id}>
                  <Link to={`/crew/detail/${crew.id}`}>
                    <div className="crewListLeft">
                      {images.length > 0 ? (
                        <img
                          src={images[0]}
                          alt={`${crew.name} 이미지`}
                          className="crewImage"
                        />
                      ) : (
                        <div>이미지 없음</div>
                      )}
                    </div>
                    <div className="crewListRight">
                      <h2>{crew.name}</h2>
                      <p>{crew.description}</p>
                      <p>{crew.district}</p>
                    </div>
                  </Link>
                </li>
              );
            })}
          </ul>
        </div>
      </div>
    </>
  );
};

export default CrewMainContainer;
