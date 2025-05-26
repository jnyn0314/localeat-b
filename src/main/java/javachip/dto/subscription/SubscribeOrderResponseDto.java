/*
 * 파일명 : SubscribeOrderResponseDto.java
 * 파일설명 : 구독 주문 조회 응답 DTO. 프론트에 표시할 구독 정보만 반환한다.
 * 작성자 : 정여진
 * 작성일 : 2025.05.26.
 */

package javachip.dto.subscription;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SubscribeOrderResponseDto {
    private String productName;     // 상품명
    private String startDate;       // 주문 생성일 (구독 시작일)
    private String deliveryCycleType;    // 배송 주기 유형 (ex. WEEKLY, MONTHLY)
    private Integer deliveryCycleValue;  // 배송 주기 값 (ex. 1, 2)
    private int quantity;    // 수량
    private Integer deliveryPeriodInMonths;  // 전체 구독 기간 (개월 단위)
}