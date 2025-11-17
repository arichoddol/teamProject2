import axios from 'axios';
import React, { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'


const BoardDetailContainer = () => {


    // boards 상태를 빈 객체로 초기화합니다.
    const [boards, setBoards] = useState({});
    const [content, setContent] = useState('');
    const [replies, setReplies] = useState([]); // 댓글 목록 상태
    const [pageInfo, setPageInfo] = useState({ // 페이지네이션 정보 상태 (first: true 추가)
        page: 0,
        size: 10,
        totalPages: 0,
        totalElements: 0,
        last: true,
        first: true, // 누락된 'first' 속성 복원
    });
    const {id} = useParams();
    const navigate = useNavigate();


    const REPLY_BASE_URL = 'http://localhost:8088/api/reply';
    const API_BASE_URL = 'http://localhost:8088/api/board';
    const IMAGE_BASE_URL = 'http://localhost:8088/upload/';
    // private static final String FILE_PATH = "C:/full/upload/";


    // 날짜 포맷 함수 (컴포넌트 내부에 정의)
    const formatDate = (dateString) => {
        if (!dateString) return '';
        return new Date(dateString).toLocaleString('ko-KR', {
            year: 'numeric', month: '2-digit', day: '2-digit',
            hour: '2-digit', minute: '2-digit'
        });
    }

    // 게시글 상세 정보를 불러오는 함수
    const fetchData = async ()=>{
      const response = await axios.get(`${API_BASE_URL}/detail/${id}`);

        if(response.data){
            setBoards(response.data);
            // 게시글 로드 성공 후, 댓글 목록 초기 로드 (1페이지)
            fetchReplies(response.data.id, 0, pageInfo.size);
        } else {
            console.log("게시물 데이터가 존재하지 않음.")
        }
    };

    // 댓글 목록을 불러오는 함수 (페이징 적용)
    const fetchReplies = async (boardId, page = 0, size = 10) => {
        if (!boardId) return;

        try {
            const response = await axios.get(
                `${REPLY_BASE_URL}/list/${boardId}?page=${page}&size=${size}&sort=createTime,desc`
            );
            
            // 데이터와 페이지 정보 업데이트
            setReplies(response.data.content);
            setPageInfo({
                page: response.data.pageable.pageNumber,
                size: response.data.pageable.pageSize,
                totalPages: response.data.totalPages,
                totalElements: response.data.totalElements,
                last: response.data.last,
                first: response.data.first,
            });

        } catch (error) {
            console.error('댓글 목록 조회 실패:', error);
            setReplies([]);
        }
    }


    useEffect(()=>{
        fetchData();  
    }, [id]);   // id가 변경될 때마다 재실행


    // 페이지네이션 버튼 클릭 핸들러
    const handlePageChange = (newPage) => {
        if (newPage >= 0 && newPage < pageInfo.totalPages) {
            fetchReplies(boards.id, newPage, pageInfo.size);
        }
    };


    const handleDelete = async () => {
        // ... (삭제 로직은 유지)
        if (!window.confirm('정말로 이 게시글을 삭제하시겠습니까?')) {
            return; 
        }

        try {
            // axios.delete를 사용하여 DELETE 요청을 보냅니다.
            const response = await axios.delete(`${API_BASE_URL}/detail/${boards.id}`);

            if (response.status === 200) {
                alert('게시글이 성공적으로 삭제되었습니다.'); 
                navigate('/board/index'); 
            } else if (response.status === 404) {
                alert('삭제할 게시글을 찾을 수 없습니다.');
            } else {
                // 서버에서 200/404 외의 상태 코드를 반환할 경우를 대비
                throw new Error(`삭제 실패: ${response.statusText}`);
            }
        } catch (error) {
            console.error('게시글 삭제 중 오류 발생:', error);
            alert('게시글 삭제 중 오류가 발생했습니다. 잠시 후 다시 시도해 주세요.');
        }
    };

    const handleUpdatePost = (boardId) =>{
        navigate(`/board/update/${boardId}`);
    }


    // 댓글 등록 핸들러 (이전 코드에서 함수가 중첩되거나 닫히지 않은 오류 수정)
    const handleReplySubmit = async(e)=>{
        e.preventDefault();

        // boards.memberId는 현재 게시글 작성자 ID를 임시로 사용 중
        if(!boards.id || !content.trim() || !boards.memberId){ 
            alert('댓글 내용 및 작성자 정보가 필요합니다.');
            return;
        }

        const replyData = {
            boardId: boards.id,
            content: content.trim(),
            memberId: boards.memberId
        };
        console.log("전송할 댓글 데이터:", replyData);

        try{
            const response = await axios.post(`${REPLY_BASE_URL}/addReply`, replyData);

            if (response.status === 200) {
                alert('댓글이 성공적으로 등록되었습니다.');
                setContent(''); // 입력창 초기화
                // 댓글 등록 성공 시, 목록을 첫 페이지(0)로 갱신하여 최신 댓글 표시
                fetchReplies(boards.id, 0, pageInfo.size); 
            } else { 
                throw new Error("댓글 등록 실패 "); 
            }
        } catch(error){
            console.error('댓글 등록 중 오류 발생:', error);
            alert('댓글 등록 중 오류가 발생했습니다. 잠시 후 다시 시도해 주세요.');
        }
    }; // <-- handleReplySubmit 함수가 여기서 올바르게 닫힙니다.

    
    return (
    
        <div className="boardDetail">
            {/* 게시글 제목 */}
            <h4>{boards.title}</h4>  
            <div className="boardDetail-con-info">
                <span>작성자 : {boards.memberNickName} </span>
                <span>조회수 : {boards.hit} </span>
                {/* formatDate 함수를 사용하여 날짜 포맷 적용 */}
                <span>작성일 : {formatDate(boards.createTime)} </span> 
            </div>
            
            <div className="boardDetail-con">
                
                {/* 💡 게시글 본문 내용을 표시하는 부분 복원 및 추가 */}
                <p className="boardDetail-content" style={{ whiteSpace: 'pre-wrap', marginBottom: '20px' }}>
                    {boards.content}
                </p> 

                <div className="boardDetail-con-image">

                    { boards.boardImgDtos && boards.boardImgDtos.length > 0 && (
                        boards.boardImgDtos.map((imgDto)=>(
                        <img 
                            // bring File by NewName Field
                            key={imgDto.id || imgDto.newName} 
                            src={`${IMAGE_BASE_URL}${imgDto.newName}`} 
                            alt={imgDto.oldName}
                            style={{ maxWidth: '100%', height: 'auto', display: 'block', margin: '10px 0' }}
                        />
                        ))
                    )}
                </div>
                
                {/* 💡 댓글 섹션 */}
                <div className="boardDetail-reply" style={{ marginTop: '30px' }}>
                    
                    {/* 1. 댓글 입력 폼 */}
                    <form onSubmit={handleReplySubmit}>
                        <textarea name="reply" id="reply"
                        rows="4" required
                        value={content}
                        onChange={(e)=> setContent(e.target.value)}
                        placeholder='댓글을 입력해주세요..'
                        ></textarea>
                        <button type="submit">댓글 등록</button>
                    </form>

                    {/* 2. 댓글 목록 표시 */}
                    <div className="reply-list mt-8 border-t pt-4">
                        <h5>댓글 ({pageInfo.totalElements})</h5>
                        {replies.length > 0 ? (
                            replies.map((reply) => (
                                <div key={reply.id} className="reply-item p-3 border-b border-gray-200">
                                    <div className="flex justify-between text-sm text-gray-600 mb-1">
                                        <p><strong>{reply.memberNickName || `작성자 ID: ${reply.memberId}`}</strong></p> 
                                        <span className="text-xs text-gray-400">{formatDate(reply.createTime)}</span>
                                    </div>
                                    <p className="text-gray-800">{reply.content}</p>
                                </div>
                            ))
                        ) : (
                            <p className="text-center text-gray-500 py-4">등록된 댓글이 없습니다.</p>
                        )}
                    </div>
                    
                    {/* 3. 페이지네이션 UI */}
                    {pageInfo.totalPages > 1 && (
                        <div className="flex justify-center items-center space-x-2 mt-4">
                            <button
                                onClick={() => handlePageChange(pageInfo.page - 1)}
                                disabled={pageInfo.first}
                                style={{ padding: '5px 10px', border: '1px solid #ccc', borderRadius: '5px' }}
                            >
                                이전
                            </button>
                            
                            <span style={{ padding: '5px 10px', background: '#eee', borderRadius: '5px', fontWeight: 'bold' }}>
                                {pageInfo.page + 1} / {pageInfo.totalPages}
                            </span>

                            <button
                                onClick={() => handlePageChange(pageInfo.page + 1)}
                                disabled={pageInfo.last}
                                style={{ padding: '5px 10px', border: '1px solid #ccc', borderRadius: '5px' }}
                            >
                                다음
                            </button>
                        </div>
                    )}
                
                </div>
                
                {/* 게시글 수정/삭제 버튼 */}
                <div className="boardDetail-act">
                    <button onClick={()=> handleUpdatePost(boards.id)}>게시글 수정</button>
                    <button onClick={handleDelete}>게시글 삭제</button>
                </div>

            </div>
        </div>
    )
}


export default BoardDetailContainer