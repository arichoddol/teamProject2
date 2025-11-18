import React, { useEffect } from 'react'
import { authDetailFn } from '../../../apis/auth/authDetail'
import { adminFn } from '../../../apis/admin/adminIndex';

const AdminIndexContainer = () => {

  useEffect(() => {
    const adminIndexFn = async () => {
      const res = await adminFn();
      console.log(res);
    }
    adminIndexFn();
  },[]);

  return (
    <div>AdminIndexContainer</div>
  )
}

export default AdminIndexContainer