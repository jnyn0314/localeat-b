package javachip.dto.order;

import javachip.entity.OrderItem;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CartOrderResponse {
    private Long orderId;
    private List<OrderCreateResponse> orderItems;

    public CartOrderResponse(Long orderId, List<OrderItem> orderItemEntities) {
        this.orderId = orderId;
        this.orderItems = orderItemEntities.stream()
                .map(OrderCreateResponse::new)
                .collect(Collectors.toList());
    }
}