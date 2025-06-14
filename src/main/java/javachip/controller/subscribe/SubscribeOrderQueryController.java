/*
 * 파일명 : SubscribeOrderQueryController.java
 * 파일설명 : 사용자 구독 주문 조회 API 컨트롤러
 * 작성자 : 정여진
 * 작성일 : 2025.05.26.
 */

package javachip.controller.subscribe;

import javachip.dto.subscription.SubscribeOrderResponseDto;
import javachip.service.subscribe.SubscribeQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders/subscription")
@RequiredArgsConstructor
public class SubscribeOrderQueryController {

    private final SubscribeQueryService subscribeQueryService;

    /**
     * [GET] /api/orders/subscription
     * 로그인된 사용자의 구독 주문 내역을 조회한다.
     *
     * @param userId 사용자 ID (쿼리 스트링 파라미터)
     * @return SubscribeOrderResponseDto 리스트
     */
    @GetMapping
    public ResponseEntity<List<SubscribeOrderResponseDto>> getSubscriptions(@RequestParam String userId) {
        List<SubscribeOrderResponseDto> result = subscribeQueryService.findSubscriptionsByUser(userId);
        return ResponseEntity.ok(result);
    }
}