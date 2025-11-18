import React, { useEffect, useRef, useState } from 'react'
import { Link } from 'react-router-dom'
import * as THREE from 'three';

import '../css/index/indexPage.css';

  const INDEX = "/images/index.png";





const IndexPage = () => {

  return (

     <div className="index">
     
                <div className="index-title">
                    <h1> 러닝크루 project</h1>
                </div>
                <div className="index-con">
                    <Link to={'/store'} className="text-draw"
                        data-text="S H O P">:: ENTER ::</Link>
                </div>
            </div>
  )
}

export default IndexPage