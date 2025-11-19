import React, { lazy, Suspense } from 'react'
import { Navigate } from 'react-router-dom'

const Loading = <div className='loading'>Loading...</div>

const AdminIndex = lazy(() => import('../components/container/admin/AdminIndexContainer'))

const AdminMember = lazy(() => import('../components/container/admin/AdminMemberListContainer'))
const AdminMemberDetail = lazy(() => import('../components/container/admin/AdminMemberDetailContainer'))

const AdminCrew = lazy(() => import('../components/container/admin/AdminCrewListContainer'))
const AdminCrewAllow = lazy(() => import('../components/container/admin/AdminCrewAllowContainer'))
const AdminCrewDetail = lazy(() => import('../components/container/admin/AdminCrewDetailContainer'))

const AdminPayment = lazy(() => import('../components/container/admin/AdminPaymentListContainer'))
const AdminPaymentDetail = lazy(() => import('../components/container/admin/AdminPaymentDetailContainer'))

const AdminAddItem = lazy(() => import('../components/container/admin/AdminAddItemContainer'))
const AdminItem = lazy(() => import('../components/container/admin/AdminItemListContainer'))
const AdminItemDetail = lazy(() => import('../components/container/admin/AdminItemDetailContainer'))

const AdminBoard = lazy(() => import('../components/container/admin/AdminBoardListContainer'))
const AdminBoardDetail = lazy(() => import('../components/container/admin/AdminBoardDetailContainer'))

const AdminAddEvent = lazy(() => import('../components/container/admin/AdminAddEventContainer'))
const AdminEvent = lazy(() => import('../components/container/admin/AdminEventListContainer'))
const AdminEventDetail = lazy(() => import('../components/container/admin/AdminEventDetailContainer'))


const toAdminRouter = () => {
    return [
        {
            path: '',
            element: <Navigate replace to={'index'} />
        },
        {
            path: 'index',
            element: <Suspense fallback={Loading}><AdminIndex /></Suspense>
        },

        // Member
        {
            path: 'memberList',
            element: <Suspense fallback={Loading}><AdminMember /></Suspense>
        },
        {
            path: 'memberDetail/:memberId',
            element: <Suspense fallback={Loading}><AdminMemberDetail /></Suspense>
        },

        // Crew
        {
            path: 'crewlist',
            element: <Suspense fallback={Loading}><AdminCrew /></Suspense>
        },
        {
            path: 'crewAllow',
            element: <Suspense fallback={Loading}><AdminCrewAllow /></Suspense>
        },
        {
            path: 'crewDetail/:crewId',
            element: <Suspense fallback={Loading}><AdminCrewDetail /></Suspense>
        },

        // Payment
        {
            path: 'paymentlist',
            element: <Suspense fallback={Loading}><AdminPayment /></Suspense>
        },
        {
            path: 'paymentDetail/:paymentId',
            element: <Suspense fallback={Loading}><AdminPaymentDetail /></Suspense>
        },

        // Item
        {
            path: 'addItem',
            element: <Suspense fallback={Loading}><AdminAddItem /></Suspense>
        },
        {
            path: 'itemlist',
            element: <Suspense fallback={Loading}><AdminItem /></Suspense>
        },
        {
            path: 'itemDetail/:itemId',
            element: <Suspense fallback={Loading}><AdminItemDetail /></Suspense>
        },

        // Board
        {
            path: 'boardlist',
            element: <Suspense fallback={Loading}><AdminBoard /></Suspense>
        },
        {
            path: 'boardDetail/:boardId',
            element: <Suspense fallback={Loading}><AdminBoardDetail /></Suspense>
        },

        // Event
        {
            path: 'addEvent',
            element: <Suspense fallback={Loading}><AdminAddEvent /></Suspense>
        },
        {
            path: 'eventlist',
            element: <Suspense fallback={Loading}><AdminEvent /></Suspense>
        },
        {
            path: 'eventDetail/:eventId',
            element: <Suspense fallback={Loading}><AdminEventDetail /></Suspense>
        },
    ];
}

export default toAdminRouter
