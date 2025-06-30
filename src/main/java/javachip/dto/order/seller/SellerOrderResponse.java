/*
파일명 : SellerOrderResponse.java
파일설명 : 판매자 마이페이지 - 주문관리 목록 조회 API 응답용 DTO
→ 프론트의 OrderManagement.jsx 에서 필요한 정보를 기반으로 구성
작성자 : 김민하
기간 : 2025-05.23.
*/
package javachip.dto.order.seller;

import javachip.entity.order.OrderItem;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SellerOrderResponse {
    private Long orderItemId;
    private Long productId;
    private String productName;
    private boolean groupbuy;
    private boolean subscribe;
    private String buyer;
    private String status;
    private int quantity;
    private int price;
    private LocalDateTime orderDate;

    public static SellerOrderResponse fromEntity(OrderItem orderItem) {
        return SellerOrderResponse.builder()
                .orderItemId(orderItem.getId())
                .productId(orderItem.getProduct().getId())
                .productName(orderItem.getProduct().getProductName())
                .groupbuy(orderItem.isGroupBuy())
                .subscribe(orderItem.isSubscription())
                .buyer(orderItem.getOrder().getUserId())
                .status(orderItem.getStatus().name())
                .quantity(orderItem.getQuantity())
                .price(orderItem.getPrice())
                .orderDate(orderItem.getOrder().getCreatedAt())
                .build();
    }
}
