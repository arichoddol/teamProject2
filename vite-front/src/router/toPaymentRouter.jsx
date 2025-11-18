// toPaymentRouter.js
import React, { lazy, Suspense } from 'react';

const Loading = <div className='loading'>Loading..</div>;
const PaymentPage = lazy(() => import('../components/container/payment/PaymentPage'));
const PaymentListPage = lazy(() => import('../components/container/payment/PaymentListPage'));
const CartPage = lazy(() => import('../components/container/cart/CartPage')); // 결제 취소시

export default function toPaymentRouter() {
  return [
    {
      path: "", // /payment 상위 경로와 합쳐져서 최종 URL은 /payment
      element: <Suspense fallback={Loading}><PaymentPage /></Suspense>
    },
    {
      path: "success",
      element: <Suspense fallback={Loading}><PaymentListPage /></Suspense>
    },
    {
      path: "cancel",
      element: <Suspense fallback={Loading}><CartPage /></Suspense>
    }
  ];
}
