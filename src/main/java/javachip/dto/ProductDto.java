package javachip.dto;

import javachip.entity.Product;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

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
    private Boolean is_group_buy;
    private Boolean is_grade_b;
    private String local;
    private String product_grade;
    private Integer delivery_fee;
    private String description;
    private Long subscription_id;
    private Long groupbuy_id;
    private Integer max_participants;
    private Long alarm_id;
    private Date create_at;
    private Integer stock_quantity;
    private String seller_id;

    public static ProductDto fromEntity(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .product_name(product.getProduct_name())
                .price(product.getPrice())
                .grade_discount_rate(product.getGrade_discount_rate())
                .subscription_discount_rate(product.getSubscription_discount_rate())
                .is_subscription(product.getIs_subscription())
                .is_group_buy(product.getIs_group_buy())
                .local(product.getLocal())
                .product_grade(product.getProduct_grade())
                .delivery_fee(product.getDelivery_fee())
                .description(product.getDescription())
                .subscription_id(product.getSubscription_id())
                .groupbuy_id(product.getGroupbuy_id())
                .max_participants(product.getMax_participants())
                .alarm_id(product.getAlarm_id())
                .create_at(product.getCreate_at())
                .stock_quantity(product.getStock_quantity())
                .seller_id(product.getSeller_id())
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
                .is_group_buy(is_group_buy)
                .local(local)
                .product_grade(product_grade)
                .delivery_fee(delivery_fee)
                .description(description)
                .subscription_id(subscription_id)
                .groupbuy_id(groupbuy_id)
                .max_participants(max_participants)
                .alarm_id(alarm_id)
                .create_at(create_at)
                .stock_quantity(stock_quantity)
                .seller_id(seller_id)
                .build();
    }
}
