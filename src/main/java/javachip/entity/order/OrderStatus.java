package javachip.entity.order;

public enum OrderStatus {
    PAID,       // 결제 완료
    READY,      // 배송 준비 중
    DELIVERING,   // 배송 중
    DELIVERED   // 배송 완료
}
