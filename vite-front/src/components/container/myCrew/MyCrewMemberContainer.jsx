import axios from 'axios'
import React, { useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import MyCrewMemberDetailModal from './MyCrewMemberDetailModal'

const MyCrewMemberContainer = () => {
  const {crewId} = useParams()
  const [ myCrewMemberList, setMyCrewMemberList] = useState([])
  const [ detailOpen, setDetailOpen] = useState(false);
  const [ detailData, setDetailData] = useState(null);

  //검색 카테고리?
  const [subject, setSubject] = useState('')
  //검색어
  const [search, setSearch] = useState('') 
  //페이징
  const [nowPage, setNowPage] = useState()
  const [startPage, setStartPage] = useState()
  const [endPage, setEndPage] = useState()
  const [totalPages, setTotalPages] = useState()


 
  // 보이고 싶은 정보 상의 후 dto 추가
  const onMyCrewMemberList = async(pageParam) =>{
    try {
      const res = await axios.get(`/api/mycrew/${crewId}/member`,
        {
          params: {
            page: pageParam, 
            subject: subject || undefined,
            search: search || undefined
          }
        })
      console.log(res.data.crewMember)
      setMyCrewMemberList(res.data.crewMember.content)
      setNowPage(res.data.nowPage)
      setStartPage(res.data.startPage)
      setEndPage(res.data.endPage)
      setTotalPages(res.data.totalPages)
      
    } catch (error) {
      console.log("내크루원 리스트 get실패")
      alert("내크루원 리스트 get실패")
    }
  }

  const onSearchClick = () => {
    // 항상 0페이지부터 다시 검색
    onMyCrewMemberList(0);
  };

  useEffect(()=>{
    onMyCrewMemberList(0)
  }, [])

  const onCrewMemberDetail = async (crewMember) => {
    try {
      const crewMemberId = crewMember.memberId
      console.log(crewMemberId)
      const res = await axios.get(`/api/mycrew/${crewId}/member/detail/${crewMemberId}`)
      console.log(res.data.crewMember)
      setDetailData(res.data.crewMember)
      setDetailOpen(true)
    } catch (error) {
      console.log("내크루원 디테일 get실패")
      alert("내크루원 디테일 get실패")
    }
    onMyCrewMemberList();
  }

  const onCrewMemberDelete = async (crewMember) => {
    try {
      const crewMemberTbId = crewMember.id
      console.log(crewMemberTbId)
      const res = await axios.get(`/api/mycrew/${crewId}/member/delete/${crewMemberTbId}`)
      console.log(res.data)
      alert(res.data)
      
    } catch (error) {
      console.log("내크루원 삭제 실패")
      alert("내크루원 삭제 실패")
    }
    onMyCrewMemberList();
  }
  return (
    <div className="myCrew">
      <div className="myCrew-con">
      <div><h2>
        크루원 명단
        </h2></div>
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
                    setSearch(value); // LEADER / MEMBER 
                  }
                }}
                >
                <option value="">::회원권한::</option>
                <option value="LEADER">크루장</option>
                <option value="MEMBER">크루원</option>
              </select>
              </li>
              {/* 그냥 검색 */}
              {/* 추가할 정보 상의 후 추가 */}
              {subject !== "status" &&(
                <li>
                <select value={subject} onChange={(e) => setSubject(e.target.value)}>
                  <option value="">::검색조건::</option>
                  <option value="id">가입순서</option>
                  <option value="memberId">회원id</option>
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
              <span>가입순서</span>
              <span>크루 ID</span>
              <span>회원 ID</span>
              <span>권한</span>
              <span>자세히보기</span>
              <span>탈퇴</span>
            </li>
          </ul>
          <ul>
            {myCrewMemberList.map((crewMember)=>(
              <li key={crewMember.id}>
              <span>{crewMember.id}</span>
              <span>{crewMember.crewId}</span>
              <span>{crewMember.memberId}</span>
              <span>{crewMember.roleInCrew}</span>
              
              {/* 크루 리더면, 탈퇴는 로그인 아이디랑 맞으면 보이게 */}
              {/* 팀장님이 리더아니면 못하게 백엔드 바꾸심 */}
              {/* 더 보이고 싶은 정보 dto에 추가 ㄱㄱ */}
              <span>
              <button type="button" onClick={()=>onCrewMemberDetail(crewMember)}>자세히 보기</button>
              </span>
              <span>
              <button type="button" onClick={()=>onCrewMemberDelete(crewMember)}>탈퇴</button>
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
                  onClick={() => onMyCrewMemberList(nowPage - 2)}>
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
                  onClick={() => onMyCrewMemberList(pageNum - 1)}
                  className={pageNum === nowPage ? "now" : ""}
                  >
                    {pageNum}
                  </button>
                ))}
                </li>

                <li>
                <button
                  disabled={nowPage === totalPages}
                  onClick={() => onMyCrewMemberList(nowPage)}>
                  다음
                </button>
                </li>
                
              </ul>
            </div>
          </div>
      </div>
          {/* 디테일 모달 ->  마찬가지로 더 보이고 싶은 정보 dto에 추가한거 가져다가 쓰면됨 */}
          {detailOpen && (
            <MyCrewMemberDetailModal
            detail={detailData}
            onClose={()=> {
              setDetailOpen(false)
              setDetailData(null)
              onMyCrewMemberList()}}
            onDelete={()=>onCrewMemberDelete(crewMemberTbId)}
            />
          )}
    </div>

  )
}

export default MyCrewMemberContainer