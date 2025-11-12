import React, { lazy, Suspense } from 'react'
import { Navigate } from 'react-router-dom'

const Loading = <div className='loading'>Loading...</div>

// Containiner
const ShopAcc = lazy(()=> import('../components/container/shop/ShopAccessoryContainer'))
const ShopCloth = lazy(()=> import('../components/container/shop/ShopClothContainer'))
const ShopEqip = lazy(()=> import('../components/container/shop/ShopEquipmentContainer'))
const ShopMain = lazy(()=> import('../components/container/shop/ShopMainContainer'))
const ShopNut = lazy(()=> import('../components/container/shop/ShopNutritionContainer'))
const ShopSale = lazy(()=> import('../components/container/shop/ShopSaleContainer'))
const ShopShoes = lazy(()=> import('../components/container/shop/ShopShoesContainer'))

const toShopRouter = () => {
  return (
    [
        {
            // shop/
            path:'',
            element: <Navigate replace to={'index'} />
        
        },
        {
          // StoreFront
            path: 'index',
            element: <Suspense fallback={Loading}><ShopMain/></Suspense>
        },
        {
            path: 'salezone',
            element: <Suspense fallback={Loading}><ShopSale/></Suspense>
        },
        {
          path:'shoes',
          element: <Suspense fallback={Loading}><ShopShoes/></Suspense>
        },
        {
          path:'cloth',
          element: <Suspense fallback={Loading}><ShopCloth/></Suspense>
        },
        {
          path:'equipment',
          element: <Suspense fallback={Loading}><ShopEqip/></Suspense>
        },
        {
          path:'accessory',
          element: <Suspense fallback={Loading}><ShopAcc/></Suspense>
        },
        {
          path:'nutrition',
          element: <Suspense fallback={Loading}><ShopNut/></Suspense>
        }
    ]
  )
}

export default toShopRouter