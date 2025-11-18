
import { Link } from 'react-router-dom'


import '../css/index/indexPage.css';

import bgImage from '../../public/images/INDEX/bg.png';
import part1 from '../../public/images/INDEX/part_w1.png';
import part3 from '../../public/images/INDEX/part_w2.png';
import { useEffect, useState } from 'react';



const IndexPage = () => {


    const [mousePosition, setMousePosition] = useState({ x: 0, y: 0 });

    useEffect(() => {
        const handleMouseMove = (event) => {
            // get viewPort Size
            const centerX = window.innerWidth / 2;
            const centerY = window.innerHeight / 2;

            const relativeX = (event.clientX - centerX) / centerX;
            const relativeY = (event.clientY - centerY) / centerY;
            setMousePosition({ x: relativeX, y: relativeY });

        };
        // widnow -> add MouseMoveListner
        window.addEventListener('mousemove', handleMouseMove);

        // when component disapper -> removeListener
        return () => {
            window.removeEventListener('mousemove', handleMouseMove);
        };
    }, []);

    const depthConfig = {
        part1: 5,   // 배경 (덜 움직임)
        part3: 20,  // 전경 (더 많이 움직임)
        title: 35,  // 텍스트 (가장 많이 움직임)
    };

    const getTransformStyle = (depth) => {
        // 마우스가 오른쪽으로 가면 요소는 왼쪽으로 이동해야 입체감이 생깁니다.
        const translateX = -mousePosition.x * depth;
        const translateY = -mousePosition.y * depth;

        return {
            transform: `translate(${translateX}px, ${translateY}px)`,
        };
    };




    return (
        <div className="title-container" style={{ backgroundImage: `url(${bgImage})` }}>

            <img
                src={part1}
                alt="Runner group"
                className="parallax-layer layer-1"
                style={getTransformStyle(depthConfig.part1)}
            />
            <img
                src={part3}
                alt="Couple running"
                className="parallax-layer layer-2"
                style={getTransformStyle(depthConfig.part3)}
            />
            <div
                className="title-text layer-3"
                style={getTransformStyle(depthConfig.title)}
            >
                <Link to={"/store"}>
                    <h1>RUNNING INTERACTIVE</h1>
                </Link >
                <p>Mouse over to see the Parallax Effect</p>
            </div>
        </div >

        // <div className="index">

        //     <div className="index-title">
        //         <h1> 러닝크루 project</h1>
        //     </div>
        //     <div className="index-con">
        //         <Link to={'/store'} className="text-draw"
        //             data-text="S H O P">:: ENTER ::</Link>
        //     </div>
        // </div>
    )
}

export default IndexPage