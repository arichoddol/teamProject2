import React, { lazy, Suspense } from 'react'
import { Navigate } from 'react-router-dom'

const Loading = <div className='loading'>Loading...</div>

// Page
const JoinMemberPage = lazy(()=> import('../components/container/auth/AuthJoin'))
const LoginPage = lazy(() => import('../components/container/auth/AuthLoginContainer'))
const LogOutPage = lazy(() => import('../components/container/auth/AuthOut'));
const AuthDetailPage = lazy(() => import('../components/container/auth/AuthDetailContainer'));

export const toLoginOutRouter = () => {
  return (
    [
        {
            // auth
            path:'',
            element: <Navigate replace to={'login'} />
        },
        {
            // auth/login
            path: 'login',
            element: <Suspense fallback={Loading}><LoginPage/></Suspense>
        },
        {
            // auth/logout
            path: 'logout',
            element: <Suspense fallback={Loading}><LogOutPage/></Suspense>
        },
        {
            path: 'join',
            element: <Suspense fallback={Loading}><JoinMemberPage/></Suspense>
        },
        {
            path: 'myPage',
            element: <Suspense fallback={Loading}><AuthDetailPage/></Suspense>
        }
    ]
  )
}
