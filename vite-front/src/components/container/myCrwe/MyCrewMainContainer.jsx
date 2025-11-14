import axios from 'axios';
import React, { useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'

const MyCrewMainContainer = () => {

  const {crewId} = useParams()
  const [myCrew , setMyCrew] = useState()

  useEffect(()=> {
    const myCrewMain = async () => {
      try {
        const res = await axios.get(`/api/mycrew/${crewId}`)
        console.log(res.data)


      } catch (error) {
        console.log("내 크루 get 실패")
      }
    }
    myCrewMain();
  }, [])

  return (
    <div className="myCrewMain">
      <div className="myCrewMain-con">
      <div style={{height: "200vh"}}>MyCrewMainContainer {crewId}</div>
      </div>
    </div>
  )
}

export default MyCrewMainContainer