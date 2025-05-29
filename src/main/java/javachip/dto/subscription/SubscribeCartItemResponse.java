/*
파일명: SubscribeCartItemResponse.java
설명: 구독 장바구니 항목에 대한 응답 DTO. 프론트에서 필요한 정보만 추려서 제공.
작성자 : 정여진
작성일 : 2025.05.29.
*/
package javachip.dto.subscription;
import javachip.entity.SubscribeCartItem;
import lombok.Getter;

@Getter
public class SubscribeCartItemResponse {

    private Long cartItemId;
    private Long productId;
    private String productName;
    private int price;
    private int quantity;
    private String deliveryCycle;
    private boolean isSelected;

    public static SubscribeCartItemResponse fromEntity(SubscribeCartItem item) {
        SubscribeCartItemResponse dto = new SubscribeCartItemResponse();
        dto.cartItemId = item.getCartItemId();
        dto.productId = item.getProduct().getId();
        dto.productName = item.getProduct().getProductName();
        dto.price = item.getProduct().getPrice();
        dto.quantity = item.getQuantity();
        dto.deliveryCycle = item.getDeliveryCycle();
        dto.isSelected = item.isSelected();
        return dto;
    }
}