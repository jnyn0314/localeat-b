/*
 * 파일명 : SubscribeOrderService.java
 * 파일설명 : 구독 주문 생성 기능 인터페이스
 * 작성자 : 정여진
 * 작성일 : 2025.05.26.
 */
package javachip.service.order;

import javachip.dto.subscription.SubscribeOrderRequest;
import javachip.dto.subscription.SubscribeOrderResponseDto;
import javachip.dto.subscription.SubscribeUpdateRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SubscribeOrderService {
    /**
     * 구독 주문을 생성하는 메서드
     *
     * @param request 클라이언트로부터 받은 구독 주문 요청 DTO
     * @param userId
     */
    void createSubscribeOrder(SubscribeOrderRequest request, String userId);

    /*
    사용자가 마이페이지에서 수정한 구독정보를 반영하는 메서드
    * */
    void updateSubscription(Long id, SubscribeUpdateRequest request);

    @Transactional(readOnly = true)
    List<SubscribeOrderResponseDto> getSubscriptions(String userId);
}