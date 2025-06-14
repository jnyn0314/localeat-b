/*
 * 파일명 : SubscribeUpdateRequest.java
 * 파일설명 : 사용자가 마이페이지에서 수정한 구독 정보를 반영하는 dto
 * 작성자 : 정여진
 * 작성일 : 2025.05.29.
 */

package javachip.dto.subscription;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubscribeUpdateRequest {
    private String deliveryCycleType; // "WEEKLY" or "MONTHLY"
    private int deliveryCycleValue;
    private int quantity;
}