/*
파일명 : OrderStatusUpdateRequest.java
파일설명 : 판매자 상품에 연결된 주문 목록 조회 / 주문 상태 수정
작성자 : 김민하
기간 : 2025-05.23.
*/
package javachip.service;

import javachip.dto.order.seller.SellerOrderResponse;

import java.util.List;

public interface SellerOrderService {

    /**
     * 판매자 ID를 기준으로, 해당 판매자가 등록한 상품에 대한 모든 주문 아이템을 조회
     */
    List<SellerOrderResponse> getOrdersBySeller(String sellerId);

    /**
     * 특정 주문 아이템의 상태를 업데이트
     * @param orderItemId 상태를 변경할 주문 아이템 ID
     * @param status 변경할 상태 값 (문자열로 전달됨)
     */
    void updateOrderStatus(Long orderItemId, String status);
}
