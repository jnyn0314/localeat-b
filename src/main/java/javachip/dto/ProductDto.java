package javachip.dto;

import javachip.entity.GradeBOption;
import javachip.entity.LocalType;
import javachip.entity.Product;
import lombok.*;

import java.util.Date;

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
    private Long subscriptionId;             // 이전: subscription_id
    private Integer maxParticipants;         // 이전: max_participants
    private Long alarmId;                    // 이전: alarm_id
    private Date createAt;                   // 이전: create_at
    private Integer stockQuantity;           // 이전: stock_quantity
    private String sellerId;                 // 이전: seller_id

    public static ProductDto fromEntity(Product p) {
        return ProductDto.builder()
                .id(p.getId())
                .productName(p.getProductName())
                .price(p.getPrice())
                .gradeDiscountRate(p.getGradeDiscountRate())
                .subscriptionDiscountRate(p.getSubscriptionDiscountRate())
                .isSubscription(p.isSubscription())
                .isGroupBuy(p.isGroupBuy())
                .local(p.getLocal())
                .productGrade(p.getProductGrade().name())
                .deliveryFee(p.getDeliveryFee())
                .description(p.getDescription())
                .subscriptionId(p.getSubscriptionId())
                .maxParticipants(p.getMaxParticipants())
                .alarmId(p.getAlarmId())
                .createAt(p.getCreateAt())
                .stockQuantity(p.getStockQuantity())
                .sellerId(p.getSellerId())
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
                .createAt(createAt)
                .stockQuantity(stockQuantity)
                .sellerId(sellerId)
                .build();
    }
}
// groupbuy랑연결해야하고,,
// status도 없어요 아직