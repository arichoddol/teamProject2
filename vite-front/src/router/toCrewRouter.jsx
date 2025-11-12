import React, { lazy, Suspense } from 'react'
import { Navigate } from 'react-router-dom'


const Loading = <div className='loading'>Loading...</div>


const CrewMain = lazy(()=> import('../components/container/crew/CrewMainContainer'))
const CrewList = lazy(()=> import('../components/container/crew/CrewListContainer'))
const CrewDetailIndex = lazy(() => import('../components/container/crew/CrewDetailIndex'))
const CrewCreateRequest = lazy(() => import('../components/container/crew/CrewCreateRequestContainer'))
const CrewBoardList = lazy(() => import('../components/container/crew/crewBoard/CrewBoardList'))

const toCrewRouter = () => {
  return (
    [
        {
            // crew/
            path:'',
            element: <Navigate replace to={'index'} />
        },
        {
            path: 'index',
            element: <Suspense fallback={Loading}><CrewMain/></Suspense>
        },
        {
            path: 'list',
            element: <Suspense fallback={Loading}><CrewList/></Suspense>
        },
        {
            path: 'detail/:id',
            element: <Suspense fallback={Loading}><CrewDetailIndex/></Suspense>
        },
        {
            path: 'createRequest',
            element: <Suspense fallback={Loading}><CrewCreateRequest/></Suspense>
        },
        {
            path: 'crewBoard/list',
            element: <Suspense fallback={Loading}><CrewBoardList/></Suspense>
        }
    ]
  )
}

export default toCrewRouter