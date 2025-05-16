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
    private int price;

    public OrderItem toOrderItem(Product product, Orders order) {
        return OrderItem.builder()
                .order(order)
                .product(product)
                .quantity(quantity)
                .isSubscription(false)
                .price(price)
                .build();
    }
}
