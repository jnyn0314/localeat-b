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
    private int originalPrice;
    private int finalPrice;
    private float gradeDiscountRate;
    private float subscriptionDiscountRate;
    private String status;
    private boolean isReviewed;  // ✅ 리뷰 관련 필드 유지

    public static OrderItemDto fromEntity(OrderItem item) {
        return OrderItemDto.builder()
                .id(item.getId())
                .productName(item.getProduct().getProductName())
                .productId(item.getProduct().getId())
                .originalPrice(item.getOriginalPrice())
                .finalPrice(item.getFinalPrice())
                .gradeDiscountRate(item.getGradeDiscountRate())
                .subscriptionDiscountRate(item.getSubscriptionDiscountRate())
                .status(item.getStatus().name())
                .isReviewed(item.isReviewed())
                .build();
    }
}
