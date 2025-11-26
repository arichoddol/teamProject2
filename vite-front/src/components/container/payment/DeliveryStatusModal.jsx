import React from "react";

const paymentStatus = {
  PENDING: "배송중",
  COMPLETED: "배송완료",
  FAILED: "결제실패",
  CANCELED: "주문취소",
  REFUNDED: "환불완료",
};

const DELIVERY_STAGES = [
  { status: paymentStatus.PENDING, label: "주문/결제 완료" },
  { status: "출고 준비", label: "상품 출고 준비" },
  { status: "운송 중", label: "상품 이동 및 운송" },
  { status: paymentStatus.COMPLETED, label: "도착 및 배송 완료" },
];

const DeliveryStatusModal = ({ payment, onClose }) => {
  const currentStatus = paymentStatus[payment.paymentStatus] || "주문 완료";
  const currentIndex = DELIVERY_STAGES.findIndex(
    (stage) => stage.status === currentStatus
  );

  // 임시 상세 시간 정보
  const getMockDetails = (status) => {
    switch (status) {
      case paymentStatus.PENDING:
        return "2025-11-25 10:00: 주문이 성공적으로 접수되었습니다.";
      case "출고 준비":
        return "2025-11-25 14:30: 물류센터에서 상품 포장 및 출고를 준비 중입니다.";
      case "운송 중":
        return "2025-11-26 09:00: 상품이 택배사 허브 터미널에서 이동 중입니다.";
      case paymentStatus.COMPLETED:
        return "2025-11-26 15:00: 고객님께 안전하게 배송이 완료되었습니다.";
      default:
        return "상세 정보 없음";
    }
  };

  const items = payment.paymentItems || [];
  const firstItemTitle = items.length > 0 ? items[0].title : "상품 정보 없음";

  return (
    <div className="modalOverlay" onClick={onClose}>
      <div className="modalContent" onClick={(e) => e.stopPropagation()}>
        <div className="modalHeader">
          <h2>📦 주문 #{payment.paymentId} 배송 조회</h2>
          <button className="closeButton" onClick={onClose}>
            &times;
          </button>
        </div>

        <div className="modalBody">
          <p className="currentStatusTag">
            현재 상태:{" "}
            <span className={`status-${currentStatus.replace(/\s/g, "")}`}>
              {currentStatus}
            </span>
          </p>

          <div className="deliverySummary">
            <p>
              주문 상품: {firstItemTitle}{" "}
              {items.length > 1 ? `외 ${items.length - 1}개` : ""}
            </p>
            <p>수령 주소: {payment.paymentAddr}</p>
          </div>

          <h4>배송 진행 상황</h4>
          <ul className="deliveryTimeline status-steps">
            {DELIVERY_STAGES.map((stage, index) => {
              const isCurrent = index === currentIndex;
              const isCompleted = index < currentIndex;

              let statusClass = "";
              if (isCompleted) statusClass = "completed";
              if (isCurrent) statusClass = "current";

              return (
                <li key={stage.status} className={statusClass}>
                  <div className="timelineDot"></div>
                  <div className="timelineDetails">
                    <span className="location">{stage.label}</span>
                    <span className="timelineStatus">
                      {isCompleted
                        ? "✅ 완료됨"
                        : isCurrent
                        ? "📍 진행 중"
                        : "⌛ 예정"}
                    </span>
                    {(isCurrent || isCompleted) && (
                      <span className="time">
                        {getMockDetails(stage.status)}
                      </span>
                    )}
                  </div>
                </li>
              );
            })}
          </ul>
        </div>
      </div>
    </div>
  );
};

export default DeliveryStatusModal;
