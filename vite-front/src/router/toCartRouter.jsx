import React, { lazy, Suspense } from 'react';

const Loading = <div className='loading'>Loading..</div>;
const CartPage = lazy(() => import('../components/container/cart/CartPage'));

export default function toCartRouter() {
  return [
    {
      path: "/cart/:memberId", // 동적 경로
      element: <Suspense fallback={Loading}><CartPage /></Suspense>
    }
  ];
}
