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
    private String status;
    private boolean isReviewed;

    public static OrderItemDto fromEntity(OrderItem item) {
        return OrderItemDto.builder()
                .id(item.getId())
                .productName(item.getProduct().getProductName())
                .productId(item.getProduct().getId())
                .status(item.getStatus().name())
                .isReviewed(item.isReviewed())
                .build();
    }
}
