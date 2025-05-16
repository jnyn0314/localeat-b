package javachip.dto.order;

import javachip.entity.OrderItem;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderItemDto {
    private Long id;
    private String productName;
    private Long productId;
    private Integer price;
    private String status;
    private boolean isReviewed;

    public static OrderItemDto fromEntity(OrderItem item) {
        return OrderItemDto.builder()
                .id(item.getId())
                .productName(item.getProduct().getProductName())
                .productId(item.getProduct().getId())
                .price(item.getPrice())
                .status(item.getStatus().name())
                .isReviewed(item.isReviewed())
                .build();
    }
}
