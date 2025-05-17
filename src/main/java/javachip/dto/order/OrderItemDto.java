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
    private int quantity;
    private Integer price;
    private String status;
    private boolean isReviewed;
    private String purchaseType;

    public static OrderItemDto fromEntity(OrderItem item) {
        String type = "일반구매";
        if (item.isSubscription()) {
            type = "구독";
        } else if (item.isGroupBuy()) {
            type = "공동구매";
        }
        return OrderItemDto.builder()
                .id(item.getId())
                .productName(item.getProduct().getProductName())
                .productId(item.getProduct().getId())
                .quantity(item.getQuantity())
                .price(item.getPrice())
                .status(item.getStatus().name())
                .isReviewed(item.isReviewed())
                .purchaseType(type)
                .build();
    }
}