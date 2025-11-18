import React, { lazy, Suspense } from 'react';

const Loading = <div className='loading'>Loading..</div>;
const CartPage = lazy(() => import('../components/container/cart/CartPage'));

export default function toCartRouter() {
  return [
    {
      path: ":memberId", // /cart/:memberId
      element: (
        <Suspense fallback={<div className='loading'>Loading..</div>}>
          <CartPage />
        </Suspense>
      ),
    },
  ];
}
