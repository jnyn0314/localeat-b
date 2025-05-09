/*
파일명 : ProductDto.java
파일설명 : 상품 정보를 주고받기 위한 DTO 클래스. Entity ↔ DTO 변환 포함.
작성자 : 정여진
기간 : 2025-05-01.
*/
package javachip.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import javachip.entity.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {
    private Long id;

    @NotBlank(message = "{product.name.notBlank}")
    private String productName;              // 이전: product_name

    @NotNull(message = "{product.price.notNull}")
    private Integer price;
    private Float gradeDiscountRate;         // 이전: grade_discount_rate
    private Float subscriptionDiscountRate;  // 이전: subscription_discount_rate

    @NotNull(message = "{product.isSubscription.notNull}")
    private Boolean isSubscription;          // 이전: is_subscription

    @NotNull(message = "{product.isGroupBuy.notNull}")
    private Boolean isGroupBuy;         // 이전: is_group_buy

    @NotNull(message = "{product.local.notNull}")
    private LocalType local;

    @NotBlank(message = "{product.productGrade.notBlank}")
    private String productGrade;             // previous enum name
    private Integer deliveryFee;             // 이전: delivery_fee

    @NotBlank(message = "{product.description.notBlank}")
    private String description;
    private Long subscriptionId;             // 이전: subscription_id
    private Integer maxParticipants;         // 이전: max_participants
    private Long alarmId;                    // 이전: alarm_id
    private Date createAt;                   // 이전: create_at
    private Integer stockQuantity;           // 이전: stock_quantity

    @NotNull(message = "{product.sellerId.notNull}")
    private String sellerId;                 // 이전: seller_id

    private String imageUrl;


    public static ProductDto fromEntity(Product p) {
        String imageUrl = null;
        if (p.getProductImages() != null && !p.getProductImages().isEmpty()) {
            imageUrl = "/api/images/" + p.getProductImages().get(0).getId(); // 썸네일처럼 첫 번째 이미지 사용
        }

        return ProductDto.builder()
                .id(p.getId())
                .productName(p.getProductName())
                .price(p.getPrice())
                .gradeDiscountRate(p.getGradeDiscountRate() != null ? p.getGradeDiscountRate() : 0.1f) // 10% 고정.
                .subscriptionDiscountRate(p.getSubscriptionDiscountRate() != null ? p.getSubscriptionDiscountRate() : 0.2f) // 20% 로 고정
                .isSubscription(p.getIsSubscription())
                .isGroupBuy(p.getIsGroupBuy())
                .local(p.getLocal())
                .productGrade(p.getProductGrade() != null ? p.getProductGrade().name() : null)
                .deliveryFee(p.getDeliveryFee() != null ? p.getDeliveryFee() : 3000) // 3000원으로 고정.
                .description(p.getDescription())
                .subscriptionId(p.getSubscriptionId())
                .maxParticipants(p.getMaxParticipants())
                .alarmId(p.getAlarmId())
                .createAt(p.getCreatedAt())
                .stockQuantity(p.getStockQuantity())
                .sellerId(p.getSeller() != null ? p.getSeller().getUserId() : null)  // seller_id를 getSeller().getId()로 수정
                .imageUrl(imageUrl)
                .build();
    }
    public Product toEntity(User seller) {
        return Product.builder()
                .id(id)
                .productName(productName)
                .price(price)
                .gradeDiscountRate(gradeDiscountRate)
                .subscriptionDiscountRate(subscriptionDiscountRate)
                .isSubscription(isSubscription)
                .isGroupBuy(isGroupBuy)
                .local(local)
                .productGrade(productGrade != null ? GradeBOption.valueOf(productGrade) : null)
                .deliveryFee(deliveryFee)
                .description(description)
                .subscriptionId(subscriptionId)
                .maxParticipants(maxParticipants)
                .alarmId(alarmId)
                .createdAt(createAt)
                .stockQuantity(stockQuantity)
                .seller((Seller) seller)
                .isGroupBuy(isGroupBuy)
                .build();
    }
}