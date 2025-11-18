import axios from 'axios';
import React, { useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'

const MyCrewMainContainer = () => {

  const {crewId} = useParams()
  const [myCrew , setMyCrew] = useState()

  useEffect(()=> {
    const myCrewMain = async () => {
      try {
<<<<<<< HEAD
        const res = await axios.get(`http://localhost:8088/api/mycrew/${crewId}`)
        console.log(res.data.crew)


      } catch (error) {
        console.log("내 크루 get 실패")
=======
        const res = await axios.get(`/api/mycrew/${crewId}`)
        console.log(res.data)
        setMyCrew(res.data.crew)

      } catch (error) {
        console.log("내 크루 get 실패")
        alert("내 크루 get 실패")
>>>>>>> dev
      }
    }
    myCrewMain();
  }, [])

  return (
    <div className="myCrewMain">
      <div className="myCrewMain-con">
<<<<<<< HEAD
      <div style={{height: "200vh"}}>MyCrewMainContainer {crewId}</div>
=======
        <div style={{height: "200vh"}}>MyCrewMainContainer {crewId}
          {/* DTO List<??Entity> 를 그대로 toDto로 받는걸 고쳐야함 
          무한참조나서 데이터를 못가져와요 */}
          {/* <ul>
            {myCrew.map((myCrew)=>(
              <li key={myCrew.id}>

              <span>{myCrew.id}</span>
            </li>
              ))}
          </ul> */}
        </div>
>>>>>>> dev
      </div>
    </div>
  )
}

export default MyCrewMainContainer