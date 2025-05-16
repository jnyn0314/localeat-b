package javachip.dto.order;

import javachip.entity.OrderItem;
import javachip.entity.Orders;
import javachip.entity.Product;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderCreateRequest {
    private Long productId;
    private int quantity;

    // 최종 가격 계산 결과를 받아 OrderItem 생성
    public OrderItem toOrderItem(Product product, Orders order, int originalPrice, int finalPrice, float gradeDiscountRate, float subscriptionDiscountRate) {
        return OrderItem.builder()
                .order(order)
                .product(product)
                .quantity(quantity)
                .isSubscription(false)
                .originalPrice(originalPrice)
                .finalPrice(finalPrice)
                .gradeDiscountRate(gradeDiscountRate)
                .subscriptionDiscountRate(subscriptionDiscountRate)
                .build();
    }
}
