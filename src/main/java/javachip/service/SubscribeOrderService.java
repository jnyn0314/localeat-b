/*
 * 파일명 : SubscribeOrderService.java
 * 파일설명 : 구독 주문 생성 기능 인터페이스
 * 작성자 : 정여진
 * 작성일 : 2025.05.26.
 */
package javachip.service;

import javachip.dto.subscription.SubscribeOrderRequest;

public interface SubscribeOrderService {
    /**
     * 구독 주문을 생성하는 메서드
     *
     * @param request 클라이언트로부터 받은 구독 주문 요청 DTO
     * @param userId
     */
    void createSubscribeOrder(SubscribeOrderRequest request, String userId);
}