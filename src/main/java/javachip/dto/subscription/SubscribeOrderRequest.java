/*
 * 파일명 : SubscribeOrderRequest.java
 * 파일설명 : 구독 주문 요청 DTO. 클라이언트에서 전송한 구독 주문 정보를 담는다.
 * 작성자 : 정여진
 * 작성일 : 2025.05.26.
 */

package javachip.dto.subscription;

import javachip.entity.order.OrderItem;
import javachip.entity.order.OrderStatus;
import javachip.entity.order.Orders;
import javachip.entity.product.Product;
import javachip.entity.subscribe.DeliveryCycle;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubscribeOrderRequest {
    private Long productId;
    private int quantity;
    private DeliveryCycleRequest deliveryCycle; // 배송 주기 정보
    private int deliveryPeriodInMonths;  // 총 구독 기간 (개월 단위)

    // enum 변환용
    public DeliveryCycle getDeliveryCycleTypeEnum() {
        return DeliveryCycle.valueOf(deliveryCycle.getCycleType() + "_" + deliveryCycle.getCycleValue());
    }

    public int getDeliveryCycleValue() {
        return deliveryCycle.getCycleValue();
    }

    /**
     * OrderItem 엔티티로 변환하는 메서드
     * @param product 상품 엔티티
     * @param order 해당 주문 엔티티
     * @return OrderItem 객체
     */
    public OrderItem toOrderItem(Product product, Orders order) {
        return OrderItem.builder()
                .order(order)
                .product(product)
                .quantity(quantity)
                .isSubscription(true)
                .deliveryCycleType(deliveryCycle.getCycleType())
                .deliveryCycleValue(deliveryCycle.getCycleValue())
                .deliveryPeriodInMonths(deliveryPeriodInMonths)
                .status(OrderStatus.PAID)
                .isGroupBuy(false)                 // 반드시.
                .isReviewed(false)                // 기본 false
                .userId(order.getUserId())        // null 아님 주의
                .price(product.getPrice())
                .build();
    }
}
