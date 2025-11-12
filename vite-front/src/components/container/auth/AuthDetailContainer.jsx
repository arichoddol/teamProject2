import React, { useEffect, useState } from 'react'
import { authDetailFn } from '../../../apis/auth/authDetail'
import { useSelector } from 'react-redux';

const AuthDetailContainer = () => {

  const [memberDetail, setMemberDetail] = useState({});

  useEffect(() => {
    const myPageDetailFn = async () => {
      const res = await authDetailFn();
      const authDetail = res.data;
      console.log(authDetail);

      if (res.status === 200) {
        setMemberDetail({...authDetail});
      }
    }
    myPageDetailFn();
  }, []);

  return (
    <div>
      <li>{memberDetail.userEmail}</li>
      <li>{memberDetail.userName}</li>
      <li>{memberDetail.gender}</li>
    </div>
  )
}

export default AuthDetailContainer