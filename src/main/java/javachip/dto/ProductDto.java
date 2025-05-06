package javachip.dto;

import javachip.entity.GradeBOption;
import javachip.entity.LocalType;
import javachip.entity.Product;
import javachip.entity.GroupBuyStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {
    private Long id;
    private String productName;              // 이전: product_name
    private Integer price;
    private Float gradeDiscountRate;         // 이전: grade_discount_rate
    private Float subscriptionDiscountRate;  // 이전: subscription_discount_rate
    private Boolean isSubscription;          // 이전: is_subscription
    private Boolean isGroupBuy;         // 이전: is_group_buy
    private LocalType local;
    private String productGrade;             // previous enum name
    private Integer deliveryFee;             // 이전: delivery_fee
    private String description;
    private LocalDateTime createdAt;
    private GroupBuyStatus groupBuyStatus;  // 추가된 필드
    private Long subscriptionId;             // 이전: subscription_id
    private Integer maxParticipants;         // 이전: max_participants
    private Long alarmId;                    // 이전: alarm_id
    private Integer stockQuantity;           // 이전: stock_quantity
    private String sellerId;                 // 이전: seller_id

    public static ProductDto fromEntity(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .productName(product.getProductName())
                .price(product.getPrice())
                .gradeDiscountRate(product.getGradeDiscountRate())
                .subscriptionDiscountRate(product.getSubscriptionDiscountRate())
                .isSubscription(product.isSubscription())
                .isGroupBuy(product.isGroupBuy())
                .local(product.getLocal())
                .productGrade(product.getProductGrade().name())
                .deliveryFee(product.getDeliveryFee())
                .description(product.getDescription())
                .subscriptionId(product.getSubscriptionId())
                .maxParticipants(product.getMaxParticipants())
                .alarmId(product.getAlarmId())
                .createdAt(product.getCreatedAt())
                .stockQuantity(product.getStockQuantity())
                .sellerId(product.getSeller() != null ? product.getSeller().getUserId() : null)  // seller_id를 getSeller().getId()로 수정
                .groupBuyStatus(product.getGroupBuy() != null ? product.getGroupBuy().getStatus() : null)
                .build();
    }

    public Product toEntity() {
        return Product.builder()
                .id(id)
                .productName(productName)
                .price(price)
                .gradeDiscountRate(gradeDiscountRate)
                .subscriptionDiscountRate(subscriptionDiscountRate)
                .isSubscription(isSubscription)
                .isGroupBuy(isGroupBuy)
                .local(local)
                .productGrade(GradeBOption.valueOf(productGrade))
                .deliveryFee(deliveryFee)
                .description(description)
                .subscriptionId(subscriptionId)
                .maxParticipants(maxParticipants)
                .alarmId(alarmId)
                .createdAt(createdAt)
                .stockQuantity(stockQuantity)
                .sellerId(sellerId)
                .build();
    }
}

// groupbuy랑연결해야하고,,
// status도 없어요 아직
//김소망이 연결하고 status도 추가함