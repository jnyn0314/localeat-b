package javachip.dto.order;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartOrderRequest {
    private List<Long> cartItemIds;  // 주문할 장바구니 항목 ID 리스트
}