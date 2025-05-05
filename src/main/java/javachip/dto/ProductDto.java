package javachip.dto;

import javachip.entity.GradeBOption;
import javachip.entity.GroupBuyOption;
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
    private String product_name;
    private Integer price;
    private Float grade_discount_rate;
    private Float subscription_discount_rate;
    private Boolean is_subscription;
    private GroupBuyOption is_group_buy;
    private LocalType local;
    private String product_grade;
    private Integer delivery_fee;
    private String description;
    private Long subscription_id;
    private GradeBOption groupbuy_id;
    private Integer max_participants;
    private Long alarm_id;
    private LocalDateTime createdAt;
    private Integer stock_quantity;
    private String seller_id;
    private GroupBuyStatus groupBuyStatus;  // 추가된 필드

    public static ProductDto fromEntity(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .product_name(product.getProduct_name())
                .price(product.getPrice())
                .grade_discount_rate(product.getGrade_discount_rate())
                .subscription_discount_rate(product.getSubscription_discount_rate())
                .is_subscription(product.getIs_subscription())
                .local(product.getLocal())
                .is_group_buy(product.getIs_group_buy())
                .product_grade(String.valueOf(product.getProduct_grade()))
                .delivery_fee(product.getDelivery_fee())
                .description(product.getDescription())
                .subscription_id(product.getSubscription_id())
                .max_participants(product.getMax_participants())
                .alarm_id(product.getAlarm_id())
                .createdAt(product.getCreatedAt())
                .stock_quantity(product.getStock_quantity())
                .seller_id(product.getSeller() != null ? product.getSeller().getUserId() : null)  // seller_id를 getSeller().getId()로 수정
                .groupBuyStatus(product.getGroupBuy() != null ? product.getGroupBuy().getStatus() : null)
                .build();
    }

    public Product toEntity() {
        return Product.builder()
                .id(id)
                .product_name(product_name)
                .price(price)
                .grade_discount_rate(grade_discount_rate)
                .subscription_discount_rate(subscription_discount_rate)
                .is_subscription(is_subscription)
                .local(local)
                .product_grade(GradeBOption.valueOf(product_grade))
                .delivery_fee(delivery_fee)
                .description(description)
                .max_participants(max_participants)
                .alarm_id(alarm_id)
                .createdAt(createdAt)
                .stock_quantity(stock_quantity)
                .seller_id(seller_id)
                .is_group_buy(is_group_buy)
                .build();
    }
}

// groupbuy랑연결해야하고,,
// status도 없어요 아직
//김소망이 연결하고 status도 추가함