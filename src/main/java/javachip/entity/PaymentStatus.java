package javachip.entity;

public enum PaymentStatus {
    PENDING,    // 결제 대기 (24시간 이내)
    COMPLETED,  // 결제 완료
    EXPIRED     // 결제 시간 만료(실패)
}
