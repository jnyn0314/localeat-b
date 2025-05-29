/**
 * 구독 장바구니 항목을 추가할 때 프론트에서 보내는 데이터를 담기 위한 것
 * */
package javachip.dto.subscription;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubscribeCartItemRequest {

    @NotNull
    private Long productId;

    @NotNull
    private Integer quantity;

    @NotNull
    private Integer price;

    @NotNull
    private String cycleType;         // WEEKLY or MONTHLY

    @NotNull
    private Integer cycleValue;       // 1, 2 등

    @NotNull
    private Integer deliveryPeriodInMonths;  // 구독 전체 기간 (ex: 1개월, 3개월)
}