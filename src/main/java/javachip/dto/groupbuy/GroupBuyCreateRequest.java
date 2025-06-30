package javachip.dto.groupbuy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupBuyCreateRequest {
    private Long productId;          // 어떤 상품에 대해 공동구매 생성하는지
    private String description;      // 소비자가 입력
    private int quantity;            // 소비자가 입력 (본인의 구매 수량)
    private LocalDate deadline;      // 소비자가 입력
}
