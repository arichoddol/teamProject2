import React, { lazy, Suspense } from 'react'
import { Navigate } from 'react-router-dom'

const Loading = <div className='loading'>Loading...</div>

const AdminIndex = lazy(()=> import('../components/container/admin/AdminIndexContainer')) 
const AdminDashBoard = lazy(()=> import('../components/container/admin/AdminDashBoardContainer')) 
const AdminMember = lazy(()=> import('../components/container/admin/AdminMemberListContainer')) 
const AdminCrew = lazy(()=> import('../components/container/admin/AdminCrewListContainer')) 
const AdminOrder = lazy(()=> import('../components/container/admin/AdminOrderListContainer')) 
const AdminPayment = lazy(()=> import('../components/container/admin/AdminPaymentListContainer')) 
const AdminAddItem= lazy(()=> import('../components/container/admin/AdminAddItemContainer')) 
const AdminItem = lazy(()=> import('../components/container/admin/AdminItemListContainer')) 



const toAdminRouter = () => {
  return (
    [
        {
            // admin/ ~
            path:'',
            element: <Navigate replace to={'index'} />
        },
        {
              // admin/index
            path: 'index',
            element: <Suspense fallback={Loading}><AdminIndex/></Suspense>
        },
        {
            path: 'dashboard',
            element: <Suspense fallback={Loading}><AdminDashBoard/></Suspense>
        },
        {
            path: 'memberList',
            element: <Suspense fallback={Loading}><AdminMember/></Suspense>
        },
        {
            path: 'crewlist',
            element: <Suspense fallback={Loading}><AdminCrew/></Suspense>
        },
        {
            path: 'orderlist',
            element: <Suspense fallback={Loading}><AdminOrder/></Suspense>
        },
        {
            path: 'paymentlist',
            element: <Suspense fallback={Loading}><AdminPayment/></Suspense>
        },
        {
            path: 'addItem',
            element: <Suspense fallback={Loading}><AdminAddItem/></Suspense>
        },
        {
            path: 'itemlist',
            element: <Suspense fallback={Loading}><AdminItem/></Suspense>
        }
    ]
  )
}

export default toAdminRouter