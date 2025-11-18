import React, { lazy, Suspense } from 'react';

const Loading = <div className='loading'>Loading..</div>;
const CartPage = lazy(() => import('../components/container/cart/CartPage'));

export default function toCartRouter() {
  return [
    {
      path: "", // 상위 /cart와 합쳐져서 최종 URL은 /cart
      element: (
        <Suspense fallback={Loading}>
          <CartPage />
        </Suspense>
      ),
    },
  ];
}

