package javachip.dto;

import javachip.entity.OrderItem;
import lombok.*;

@Getter
@Setter
@Builder
public class OrderItemDto {
    private Long id;
    private String productName;
    private Long productId;

    public static OrderItemDto fromEntity(OrderItem oi) {
        return OrderItemDto.builder()
                .id(oi.getId())
                .productName(oi.getProduct().getProductName())
                .productId(oi.getProduct().getId())
                .build();
    }
}
