import axios from 'axios';
import React, { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router';

import "../../../css/store/storeDetail.css"

const ShopDetailContainer = () => {

    const NO_IMAGE_URL = "/images/noimage.jpg";
    const IMAGE_BASE_URL = null

    const [item, setItem] = useState({});
    const [content, setContent] = useState('');
    const [replies, setReplies] = useState([]);
       const [pageInfo, setPageInfo] = useState({ // 페이지네이션 정보 상태 (first: true 추가)
            page: 0,
            size: 10,
            totalPages: 0,
            totalElements: 0,
            last: true,
            first: true, 
        });

    const { id } = useParams();
    const navigate = useNavigate();

    const REPLY_BASE_URL = 'http://localhost:8088/api/itemReply';
    const API_BASE_URL = 'http://localhost:8088/api/shop';

     const formatDate = (dateString) => {
        if (!dateString) return '';
        return new Date(dateString).toLocaleString('ko-KR', {
            year: 'numeric', month: '2-digit', day: '2-digit',
            hour: '2-digit', minute: '2-digit'
        
        });
    }




    const fetchData = async () => {
        const response = await axios.get(`${API_BASE_URL}/detail/${id}`);

        if (response.data) {
            setItem(response.data);
            fetchReplies(response.data.id, 0, pageInfo.size);

        } else {
            console.log("게시물 데이터가 존재하지 않음.")
        }
    };

    const fetchReplies = async (itemId, page=0, size=10) =>{ 
        if(!item) return;

        try{
            const response = await axios.get(
                 `${REPLY_BASE_URL}/list/${itemId}?page=${page}&size=${size}&sort=createTime,desc`
            );
        
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
            console.log('댓글 목록 조회 실패: ', error);
            setReplies([]);
        }
    }


    const handleReplySubmit = async(e) => {
        e.preventDefault();
    
          if(!item.id || !content.trim() || !item.memberId){ 
            alert('댓글 내용 및 작성자 정보가 필요합니다.');
            return;
        }

        const replyData = {
            itemId: item.id,
            content: content.trim(),
            memberId: item.memberId
        };
        console.log("전송할 댓글 데이터:", replyData);

        try{
             const response = await axios.post(`${REPLY_BASE_URL}/addReply`, replyData);

             if (response.status === 200) {
                alert('댓글이 성공적으로 등록되었습니다.');
                setContent('');
                fetchReplies(item.id, 0, pageInfo.size); 
            } else { 
                throw new Error("댓글 등록 실패 "); 
            }
        } catch(error){
          console.error('댓글 등록 중 오류 발생:', error);
          alert('댓글 등록 중 오류가 발생했습니다. 잠시 후 다시 시도해 주세요.');  
        }
    };

    useEffect(() => {
        fetchData();
        // when id change its always restart it.
    }, [id]);

    return (
        <div className="itemDetail">
            {console.log(item)}
         
            <div className="itemDetail-con">
                
                <div className="itemDetail-con-image">
                    {/* {item.attachFile && item.attachFile !== 0 ?(
                        <img 
                        src={null}
                        alt={item.itemTitle || "첨부 이미지"}
                         />
                    ):(
                        <img 
                        src={NO_IMAGE_URL}
                        alt="이미지 없음"
                        />
                    )} */}
                </div>
                <div className="itemDetail-con-info">
                    <h4>{item.itemTitle}</h4>
                    <span>상품ID : {item.id}</span><br />
                    <span>상세설명 : {item.itemDetail}</span><br />
                    <span>상품가격 : {item.itemPrice}</span><br />
                    <span>itemSize(temp) : {item.itemSize}</span><br />
                    <span>attachFile(temp) : {item.attachFile}</span><br />
                    <span>createTime : {item.createTime}</span><br />
                    <span>updateTime : {item.updatTime}</span><br />
                </div>
                 <div className="itemDetail-con-reply">

                    <form onSubmit={handleReplySubmit}>
                        <textarea name="reply" id="reply"
                        rows="4" required
                        value={content}
                        onChange={(e)=> setContent(e.target.value)}
                        placeholder='댓글을 입력해주세요..'
                        ></textarea>
                        <button type='submit'>댓글등록</button>
                    </form>
                  </div>
                  <div className="replyList">
                    <h5>댓글 ({pageInfo.totalElements})</h5>
                     {replies.length > 0 ? (
                            replies.map((reply) => (
                                <div key={reply.id} >
                                    <div className='tab'>
                                        {console.log(reply)}
                                        <p><strong>{reply.memberNickName || `작성자 ID: ${reply.memberId}`}</strong></p> 
                                        <span className="text-xs">{formatDate(reply.createTime)}</span>
                                    </div>
                                    <p className="text-gray-800">{reply.content}</p>
                                </div>
                            ))
                        ) : (
                            <p className="text-center text-gray-500 py-4">등록된 댓글이 없습니다.</p>
                        )}

                    {/* 3. Pagenation UI */}
                    {pageInfo.totalPages > 1 && (
                        <div className="tab">
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
            </div>
            
        </div>
        
        
    )



}

export default ShopDetailContainer