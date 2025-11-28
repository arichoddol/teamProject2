import React from 'react'
import Header from '../components/common/Header'
import { Outlet } from 'react-router-dom'
import Footer from '../components/common/Footer'
import MyCrewLeftContainer from '../components/container/myCrew/MyCrewLeftContainer'

const MyCrewLayout = () => {
  return (
    

    <div className="mycrew">
      <div className="mycrew-con">
        
        <div className="left">
          <MyCrewLeftContainer/>
        </div>

        <div className="right">
          <Header/> 
          <Outlet/>
          
        </div>

      </div>
    </div>
    
  )
}

export default MyCrewLayout