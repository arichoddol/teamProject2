import axios from "axios";
import React, { useEffect, useRef, useState } from "react";
import { Link } from "react-router";
import SilderInner from "./SilderInner";

import "../../../css/store/storeIndex.css";
import "../../../css/store/storeIndexSlide.css";




const ShopMainContainer = () => {

  

  const NO_IMAGE_URL = "/images/noimage.jpg";


  const sliderRef = useRef(null);

  const [items, setItems] = useState([]);
  const [currentPage, setCurrentPage] = useState(0);
  const [pageInfo, setPageInfo] = useState({
    totalPages: 0,
    startPage: 0,
    endPage: 0,
    currentPage: 0,
  });

  const displayPageNum = 5; // 화면에 표시할 페이지 버튼 개수
  

  const fetchData = async (page) => {
    const response = await axios.get(
      `http://localhost:8088/api/shop?page=${page}`
    );
    const data = response.data;
    console.log(`[LOG] 페이지 ${page + 1}의 데이터를 요청합니다.`);

    try {
      if (data && data.content) {
        setItems(data.content || []);

        // 페이지 정보 계산 및 업데이트
        const totalPages = data.totalPages;
        const pageNum = data.number;
        const displayPageNum = 5;

        const startPage = Math.floor(pageNum / displayPageNum) * displayPageNum;
        let endPage = startPage + displayPageNum - 1;
        if (endPage >= totalPages) {
          endPage = totalPages - 1;
        }
        setPageInfo({
          currentPage: pageNum,
          totalPages: totalPages,
          startPage: startPage,
          endPage: endPage,
        });
      }
    } catch (error) {
      console.error("상품 데이터 로드 실패:", error);
      setItems([]);
    }
  };


  useEffect(() => {

    fetchData(currentPage);
  }, [currentPage]);

  const pageNumbers = [];
  for (let i = pageInfo.startPage; i <= pageInfo.endPage; i++) {
    pageNumbers.push(i);
  }
  const handlePageClick = (page) => {
    // When PageChange -> scroll is move to Top = UI Improvment
    window.scrollTo({ top: 0, behavior: "smooth" });
    setCurrentPage(page);
  };

  const handleSliderPrev = (e) => {
        e.preventDefault();
        if (sliderRef.current && sliderRef.current.prev) {
            sliderRef.current.prev();
        }
    };

    const handleSliderNext = (e) => {
        e.preventDefault();
        if (sliderRef.current && sliderRef.current.next) {
            sliderRef.current.next();
        }
    };

  return (
    <div className="itemList">
      <br />
      <div className="itemList-con">
        <div className="itemList-banner">
          {/* image Header slice here.....*/}
          <div className="main">
              <div className="slider__wrap">
                  <div className="slider__img">
                     <div className="slider__thumb"></div>  
                      <div className="slider__btn">
                        <a href="#" className="prev" title="이전이미지" onClick={handleSliderPrev}>«</a>
                        <a href="#" className="next" title="다음이미지" onClick={handleSliderNext}>»</a>
                      </div>
                          <SilderInner ref={sliderRef} silderInterval={2000}>
                            <div className="slider s1"><img src="/images/store/swiper/header1.jpg" alt="이미지1"/></div>
                            <div className="slider s2"><img src="/images/store/swiper/header2.jpg" alt="이미지2"/></div>
                            <div className="slider s3"><img src="/images/store/swiper/header3.jpg" alt="이미지3"/></div>
                            <div className="slider s4"><img src="/images/store/swiper/header4.jpg" alt="이미지4"/></div>
                            <div className="slider s5"><img src="/images/store/swiper/header5.jpg" alt="이미지5"/></div>
                          </SilderInner>
                  </div>
              </div>
          </div>
        </div>
        <h2> :: 상품목록 :: </h2>
        <br />
        <div className="item-grid-container">
          {items.length === 0 && (
            <p className="no-items-data">등록된 상품이 없습니다.</p>
          )}
          
          {items.map((list) => (
            <Link
              to={`/store/detail/${list.id}`}
              key={list.id}
              className="item-card-link">
              <div className="item-card">
                {/* 상품 이미지 영역 */}
                {console.log(items)}
                {list.attachFile === 1 ? (
                  <div className="item-image-placeholder">
                    {list.itemImgDtos.map((imgDto, index) => (
                      <img
                        key={index} // 리액트 반복문에서는 key가 필수입니다.
                        src={imgDto.fileUrl} // 👈 배열의 각 요소(imgDto)에 접근
                        alt={imgDto.oldName}
                        className="gallery-image"
                    />
                    ))}
                  </div>
                ) : (
                  <img
                    src={NO_IMAGE_URL}
                    alt="이미지 없음"
                    width="250"
                    height="250"
                    className="item-image"
                  />
                )}
                <span className="no-image-text"></span>
                <div className="item-info">
                  <h4 className="item-title">{list.itemTitle}</h4>
                  <p className="item-price">
                    {list.itemPrice
                      ? list.itemPrice.toLocaleString()
                      : "가격 미정"}{" "}
                    원
                  </p>
                </div>
              </div>
            </Link>
          ))}
        </div>
        {/* E.O.F item-grid-container */}

        {/* Paging Section */}
        {pageInfo.totalPages > 1 && (
          <div className="pagination-container">
            {/* 이전 페이지 블록으로 이동 (<) */}
            <button
              className="page-btn page-prev"
              onClick={() => handlePageClick(pageInfo.startPage - 1)}
              disabled={pageInfo.startPage === 0}
            >
              &lt;
            </button>

            {/* 페이지 번호 버튼들 */}
            {pageNumbers.map((page) => (
              <button
                key={page}
                className={`page-btn ${page === currentPage ? "active" : ""}`}
                onClick={() => handlePageClick(page)}
              >
                {page + 1}
              </button>
            ))}

            {/* 다음 페이지 블록으로 이동 (>) */}
            <button
              className="page-btn page-next"
              onClick={() => handlePageClick(pageInfo.endPage + 1)}
              disabled={pageInfo.endPage >= pageInfo.totalPages - 1}
            >
              &gt;
            </button>
          </div>
        )}
        {/* E.O.F pagination-container */}
      </div>
    </div>
  );
};

export default ShopMainContainer;
