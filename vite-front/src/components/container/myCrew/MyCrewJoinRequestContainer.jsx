import axios from 'axios'
import React, { useEffect, useState } from 'react'
import { useSelector } from 'react-redux'
import { useParams } from 'react-router-dom'

const MyCrewJoinRequestContainer = () => {
  const {accessToken} = useSelector((state) => state.jwtSlice)

  const {crewId} = useParams()
  const [myCrewJoinRequestList , setMyCrewJoinRequestList] = useState([])
  //검색 카테고리?
  const [subject, setSubject] = useState('')
  //검색어
  const [search, setSearch] = useState('') 

  const [nowPage, setNowPage] = useState()
  const [startPage, setStartPage] = useState()
  const [endPage, setEndPage] = useState()
  const [totalPages, setTotalPages] = useState()
  // 크루가입요청 리스트
  const MyCrewjoinRequest = async (pageParam) => {
    try {
      const res = await axios.get(`/api/mycrew/${crewId}/joinRequest`,
      {
        params: {
          page: pageParam, 
          subject: subject || undefined,
          search: search || undefined
        }
      })
      console.log(res.data)
      setMyCrewJoinRequestList(res.data.myCrewJoinList.content)
      setNowPage(res.data.nowPage)
      setStartPage(res.data.startPage)
      setEndPage(res.data.endPage)
      setTotalPages(res.data.totalPages)
      
    } catch (error) {
      console.log("내 크루 가입요청 get 실패")
      alert("내 크루 가입요청 get 실패")
    }
  }

  const onSearchClick = () => {
    // 항상 0페이지부터 다시 검색
    MyCrewjoinRequest(0);
  };
  
  useEffect(()=> {
    MyCrewjoinRequest();
  }, [])


  //크루가입승인
  const onJoinApproved = async (joinReq) =>{
    console.log(joinReq)
      try {
        const res = await axios.post(`/api/mycrew/${crewId}/joinRequest/approved`,
          { crewRequestId: crewId,
            memberRequestId: joinReq.memberRequestId,
            message: joinReq.message },
          { headers: { 
              "Authorization": `Bearer ${accessToken}`,
              "Content-Type": "application/json" 
            } 
          }

        )
        console.log(res.data)
        
      } catch (error) {
        console.log(error)
        console.log("내 크루 가입승인 실패")
        alert("내 크루 가입승인 실패")
      }
      alert("내 크루 가입승인 성공")
      MyCrewjoinRequest(0);
  }
  //크루가입거절
  const onJoinRejected =async (joinReq) =>{

      try {
        const res = await axios.post(`/api/mycrew/${crewId}/joinRequest/rejected`,
          { crewRequestId: crewId,
            memberRequestId: joinReq.memberRequestId,
            message: joinReq.message},
          { headers: { 
              "Authorization": `Bearer ${accessToken}`,
              "Content-Type": "application/json" 
            } 
          }

        )
        console.log(res.data)
        
      } catch (error) {
        console.log("내 크루 가입거절 실패")
        alert("내 크루 가입거절 실패")
      }
      alert("내 크루 가입거절 성공")
      MyCrewjoinRequest(0)     
  }
  
  return (
    <div className="myCrew">
      <div className="myCrew-con">
        <div><h2>
          크루 가입 신청 명단</h2></div>
          <div className="myCrew-search">
            <ul>
              {/* 상태검색 */}
              <li>
              <select
                value={search}
                onChange={(e) => {
                  const value = e.target.value;
                  if (value === "") {
                    setSubject("");
                    setSearch("");
                  } else {
                    setSubject("status");
                    setSearch(value); // PENDING / APPROVED / REJECTED
                  }
                }}
                >
                <option value="">::상태선택::</option>
                <option value="PENDING">대기</option>
                <option value="APPROVED">승인</option>
                <option value="REJECTED">거절</option>
              </select>
              </li>
              {/* 그냥 검색 */}
              {subject !== "status" &&(
                <li>
                <select value={subject} onChange={(e) => setSubject(e.target.value)}>
                  <option value="">::검색조건::</option>
                  <option value="id">신청순서</option>
                  <option value="memberRequestId">회원id</option>
                  <option value="message">메시지</option>
                </select>
              </li>

              )}

              <li>
                <input
                    type="text"
                    value={search}
                    onChange={(e) => setSearch(e.target.value)}
                    placeholder="검색어 입력"
                    />
              </li>
              <li>
                <button type='button' onClick={onSearchClick}>검색</button>
              </li>

            </ul>
          </div>

          <div className="myCrew-content">
         
            <ul>

              <li>

              <span>신청순서 </span>
              <span>회원 ID </span>
              <span>가입 메시지</span>
              <span>상태 </span>
              <span>가입승인</span>
              <span>가입거절</span>  
              </li>
            </ul>
            
              {/* 리스트 */}
            <ul>
              {/* 넣고 싶은 정보 더 넣으면 됨 dto에 안한거임 */}
            {myCrewJoinRequestList.map((joinReq)=>(
              <li key={joinReq.id}>

              <span>{joinReq.id}</span>
              <span>{joinReq.memberRequestId}</span>
              <span>{joinReq.message}</span>
              <span>{joinReq.status}</span>
              <span>

              <button type="button" onClick={()=>onJoinApproved(joinReq)}>가입승인</button>
              </span>
              <span>
              <button type="button" onClick={()=>onJoinRejected(joinReq)}>가입거절</button>

              </span>
              </li>

            ))}
          </ul>
        </div>
          <div className="myCrew-paging">
            <div className="myCrew-paging-con">
              <ul>
                <li>총페이지 : {totalPages}</li>
                <li>
                  <button
                    disabled={nowPage === 1}
                    onClick={() => MyCrewjoinRequest(nowPage - 2)}>
                    이전
                  </button>
                </li>

                <li>
                  {Array.from(
                    { length: endPage - startPage + 1 },
                    (_, idx) => startPage + idx
                  ).map((pageNum) => (
                    <button
                      key={pageNum}
                      onClick={() => MyCrewjoinRequest(pageNum - 1)}
                      className={pageNum === nowPage ? "now" : ""}
                      >
                      {pageNum}
                    </button>
                  ))}
                </li>
                <li>
                  <button
                    disabled={nowPage === totalPages}
                    onClick={() => MyCrewjoinRequest(nowPage)}>
                    다음
                  </button>
                </li>
                
              </ul>
            </div>
          </div>
      </div>
    </div>
  )
    
}

export default MyCrewJoinRequestContainer