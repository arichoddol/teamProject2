import React, { forwardRef, useEffect, useImperativeHandle, useRef, useState } from 'react'

const SilderInner = forwardRef (({ children, silderInterval = 2000 }, ref) => {

    const sliderInnerRef = useRef(null);
    const [currentIndex, setCurrentIndex] = useState(0);
    const slides = React.Children.toArray(children);
    const sliderCount = slides.length;
    const [sliderWidth, setSliderWidth] = useState(0);

    

    const handlePrev = (e) => {
        const prevIndex = currentIndex === 0 ? sliderCount : currentIndex - 1;
        setCurrentIndex(prevIndex);
    };

    const handleNext = (e) => {
      setCurrentIndex(currentIndex + 1);
    };
    const handleThumbnailClick = (index) => {
        // 점 클릭 시 해당 인덱스로 직접 이동
        setCurrentIndex(index); 
    };

    useImperativeHandle(ref, () => ({
   
        prev: handlePrev,
        next: handleNext,
    }));

  
    


    useEffect(()=>{
        if(sliderInnerRef.current){
            const firstSlide = sliderInnerRef.current.querySelector(".slider");
            if(firstSlide){
                setSliderWidth(firstSlide.clientWidth);
            }
        }

        const intervalId =setInterval(()=>{
            setCurrentIndex(prevIndex => prevIndex+1);
        }, silderInterval);

        // when component Unmount
        return ()=> clearInterval(intervalId);
    },[silderInterval]) // reset When interval change

    useEffect(()=>{
        const inner = sliderInnerRef.current;
        if(!inner || sliderWidth == 0){ return; }

        const transitionDuration = '0.6s';

        if (currentIndex < sliderCount) {
            inner.style.transition = `transform ${transitionDuration}`;
            inner.style.transform = `translateX(-${sliderWidth * currentIndex}px)`;
        }
        else if (currentIndex === sliderCount) {
            inner.style.transition = `transform ${transitionDuration}`;
            inner.style.transform = `translateX(-${sliderWidth * currentIndex}px)`;

        setTimeout(() => {
                inner.style.transition = 'none'; // 트랜지션 제거
                inner.style.transform = 'translateX(0px)'; // 0으로 즉시 이동
                setCurrentIndex(0); // 인덱스를 0으로 리셋 (화면에는 안보임)
            }, parseFloat(transitionDuration) * 1000); // 트랜지션 시간에 맞춰 지연
        }
    }, [currentIndex, sliderCount, sliderWidth]);

    const sliderClone = 
        React.cloneElement
            (slides[0], { key: 'clone-0', className: `${slides[0].props.className} clone-slide` });
    
    const displayIndex = (currentIndex % sliderCount) + 1;        

    // this for UI Dot Pagenation -> element create
    const dotElements = Array.from({ length: sliderCount }).map((_, index) => (
        <span
            key={`dot-${index}`}
            className={`slider__dot ${index === (currentIndex % sliderCount) ? 'active' : ''}`}
            onClick={() => handleThumbnailClick(index)} 
        />
    ));


  return (
    <>
        <div className="slider__count">
            <span className="current">{displayIndex}</span>
            <span className="separator"> / </span>
            <span className="total">{sliderCount}</span>
        </div>
        <div className="slider__pagination">
                {dotElements}
            </div>
         <div className="slider__inner" ref={sliderInnerRef}>
            {children}
            {sliderClone} 
        </div>
        </>
  )
})
export default SilderInner