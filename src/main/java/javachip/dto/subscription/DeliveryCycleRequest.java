/*
 * 파일명 : DeliveryCycleRequest.java
 * 파일설명 : 구독 배송 주기 정보 DTO. 주기 유형과 값을 담는다.
 * 작성자 : 정여진
 * 작성일 : 2025.05.26.
 */

package javachip.dto.subscription;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeliveryCycleRequest {
    private String cycleType;   // 예: WEEKLY, MONTHLY
    private int cycleValue;     // 예: 1, 2, 3
}
