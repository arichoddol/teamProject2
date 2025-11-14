import React, { lazy, Suspense } from 'react'
import { createBrowserRouter } from 'react-router-dom'



import { toLoginOutRouter } from './toLoginOutRouter'
import toCrewRouter from './toCrewRouter'
import toAdminRouter from './toAdminRouter'
import toBoardRouter from './toBoardRouter'
import toShopRouter from './toShopRouter'
import toEventRouter from './toEventRouter'
import toMyCrewRouter from './toMyCrewRouter'

const Loading = <div className='loading'>Loading..</div>


// Layout
const ShopLayout = lazy(()=> import(`../layout/ShopLayout`))
const LoginLayout = lazy(()=> import(`../layout/LoginLayout`))
const AdminLayout = lazy(()=> import(`../layout/admin/AdminLayout`))
const BoardLayout = lazy(()=> import(`../layout/BoardLayout`))
const EventLayout = lazy(()=> import(`../layout/EventLayout`))
const CrewLayout = lazy(()=> import('../layout/CrewLayout'))

const CartLayout = lazy(()=> import(`../layout/CartLayout`))
const PaymentLayout = lazy(()=> import(`../layout/PaymentLayout`))

const MyCrewLayout = lazy(()=> import('../layout/MyCrewLayout'))


// Page
const IndexPage = lazy(()=>import(`../pages/IndexPage`))
const CartPage = lazy(() => import(`../components/container/cart/CartPage`))
const PaymentPage = lazy(() => import(`../components/container/payment/PaymentPage`))
const PaymentListPage = lazy(() => import(`../components/container/payment/PaymentListPage`))


const root = createBrowserRouter([

    {
        //index
        path:'',
        element: <Suspense fallback={Loading}><IndexPage/></Suspense>
    },
    {
        // Admin
        path:'admin',
        element: <Suspense fallback={Loading}><AdminLayout/></Suspense>,
        children: toAdminRouter()
    },
    {
        // Auth 
        path: 'auth',
        element: <Suspense fallback={Loading}><LoginLayout/></Suspense>,
        children: toLoginOutRouter()
    },
    {
        // shop
        path: 'shop',
        element: <Suspense fallback={Loading}><ShopLayout/></Suspense>,
        children: toShopRouter()
    },
    {
        // crew
        path:'crew',
        element: <Suspense fallback={Loading}><CrewLayout/></Suspense>,
        children: toCrewRouter()
    },
    {
        // board ( Community )
        path:'board',
        element: <Suspense fallback={Loading}><BoardLayout/></Suspense>,
        children: toBoardRouter()
    },
    {
        // event ( 대회일정 )
        path:'event',
        element: <Suspense fallback={Loading}><EventLayout/></Suspense>,
        children: toEventRouter()
    },
    
    {
        // mycrew
        path:'mycrew/:crewId',
        element: <Suspense fallback={Loading}><MyCrewLayout/></Suspense>,
        children: toMyCrewRouter()
    }
])
    
export default root
