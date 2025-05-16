package javachip.dto.subscription;

import javachip.entity.OrderItem;
import javachip.entity.Orders;
import javachip.entity.Product;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubscribeOrderRequest {
    private Long productId;
    private int quantity;
    private DeliveryCycleRequest deliveryCycle;
    private int deliveryPeriodInMonths;

    // OrderItem 변환 메서드 (구독 주문)
    public OrderItem toOrderItem(Product product, Orders order) {
        return OrderItem.builder()
                .order(order)
                .product(product)
                .quantity(quantity)
                .isSubscription(true)
                .build();
    }
}
