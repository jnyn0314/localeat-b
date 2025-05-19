/*
파일명 : ProductDto.java
파일설명 : 상품 정보를 주고받기 위한 DTO 클래스. Entity ↔ DTO 변환 포함.
작성자 : 정여진
기간 : 2025-05-01.
*/
package javachip.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import javachip.entity.*;
import lombok.*;

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
    private Float gradeDiscountRate;
    private Float subscriptionDiscountRate;

    @NotNull(message = "{product.isGroupBuy.notNull}")
    private Boolean isGroupBuy;         // 이전: is_group_buy

    @NotNull(message = "{product.local.notNull}")
    private LocalType local;

    @NotBlank(message = "{product.productGrade.notBlank}")
    private String productGrade;
    private Integer deliveryFee;

    @NotBlank(message = "{product.description.notBlank}")
    private String description;
    private Long subscriptionId;
    private Integer maxParticipants;
    private Long alarmId;
    private Date createAt;
    private Integer stockQuantity;

    @NotNull(message = "{product.sellerId.notNull}")
    private String sellerId;
    private String imageUrl;

    private Boolean isWished; // 찜 여부
    private Long wishId;      // 찜 엔티티 ID

    /**
     * Product 엔티티 → ProductDto 변환 (찜 정보 포함)
     */
    public static ProductDto fromEntity(Product p, Boolean isWished, Long wishId) {
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
                .isWished(isWished)  // 찜 여부 (null 허용)
                .wishId(wishId)      // 찜 ID (null 허용)
                .build();
    }

    /**
     * Product 엔티티 → ProductDto 변환 (찜 정보 없이 기본 변환)
     */
    public static ProductDto fromEntity(Product p) {
        return fromEntity(p, null, null);
    }

    /**
     * ProductDto → Product 엔티티로 변환
     */
    public Product toEntity(User seller) {
        return Product.builder()
                .id(id)
                .productName(productName)
                .price(price)
                .gradeDiscountRate(gradeDiscountRate)
                .subscriptionDiscountRate(subscriptionDiscountRate)
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