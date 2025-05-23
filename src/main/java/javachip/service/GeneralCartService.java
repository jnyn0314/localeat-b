package javachip.service;

import javachip.dto.order.consumer.CartItemRequest;
import javachip.dto.order.consumer.CartItemResponse;
import javachip.dto.order.consumer.CartOrderRequest;
import javachip.dto.order.consumer.OrderCreateResponse;

import java.util.List;

public interface GeneralCartService {

    /**
     * 일반구매 장바구니 항목 추가
     * @param userId 사용자 ID
     * @param request 장바구니 추가 요청 정보
     */
    void addToCart(String userId, CartItemRequest request);

    /**
     * 일반구매 장바구니 목록 조회
     * @param userId 사용자 ID
     * @return 장바구니 항목 리스트
     */
    List<CartItemResponse> getCartItems(String userId);

    /**
     * 일반구매 장바구니 항목 삭제
     * @param cartItemId 장바구니 항목 ID
     */
    void deleteCartItem(Long cartItemId);

    /**
     * 일반구매 장바구니 항목 주문
     * @param userId 사용자 ID
     * @param request 주문 요청 정보 (선택된 장바구니 항목 ID 리스트)
     * @return 주문 결과
     */
    List<OrderCreateResponse> orderFromCart(String userId, CartOrderRequest request);
}
