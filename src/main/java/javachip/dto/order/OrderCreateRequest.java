package javachip.dto.order;

import javachip.entity.OrderItem;
import javachip.entity.Orders;
import javachip.entity.Product;
import javachip.entity.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderCreateRequest {
    private String userId;
    private Long productId;
    private int quantity;
    private int price;
    private OrderStatus status = OrderStatus.PAID;

    public OrderItem toOrderItem(Product product, Orders order) {
        return OrderItem.builder()
                .userId(userId)
                .order(order)
                .product(product)
                .quantity(quantity)
                .isSubscription(false)
                .price(price)
                .status(status)
                .build();
    }
}
