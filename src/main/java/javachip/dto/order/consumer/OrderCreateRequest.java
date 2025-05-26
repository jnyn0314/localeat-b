package javachip.dto.order.consumer;

import javachip.entity.*;
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
        System.out.println("[DEBUG] toOrderItem 호출됨: " + order.getUserId());
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

    /**
     * 장바구니 아이템(CartItem) 기반으로 주문 아이템(OrderItem) 생성 메서드
     * - 일반구매 장바구니 주문 처리 시 사용
     */
    public static OrderItem fromCartItem(CartItem cartItem) {
        return OrderItem.builder()
                .product(cartItem.getProduct())
                .quantity(cartItem.getQuantity())
                .isSubscription(false) // 일반구매
                .isGroupBuy(false)     // 일반구매
                .userId(cartItem.getCart().getConsumer().getUserId())
                .status(OrderStatus.PAID)
                .isReviewed(false)
                .price(cartItem.getProduct().getPrice()) // 필요 시 할인 반영 가능
                .build();
    }
}
