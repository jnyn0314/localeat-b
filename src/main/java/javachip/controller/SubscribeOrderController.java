/*
 * 파일명 : SubscribeOrderController.java
 * 파일설명 : 구독 주문 API 컨트롤러. POST 요청을 받아 서비스 호출 후 응답 반환
 * 작성자 : 정여진
 * 작성일 : 2025-05-26
 */

package javachip.controller;

import jakarta.validation.Valid;
import javachip.dto.subscription.SubscribeOrderRequest;
import javachip.service.SubscribeOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/subscribe-order")
@RequiredArgsConstructor
public class SubscribeOrderController {

    private final SubscribeOrderService subscribeOrderService;

    /**
     * [POST] /api/subscribe-order
     * 클라이언트로부터 구독 주문 요청을 받아 처리
     */
    @PostMapping
    public ResponseEntity<Void> createSubscribeOrder(
            @RequestHeader("userId") String userId,
            @RequestBody SubscribeOrderRequest request
    ) {
        System.out.println("== userId: " + userId);
        System.out.println("== request: " + request);
        subscribeOrderService.createSubscribeOrder(request, userId);
        return ResponseEntity.ok().build();
    }
}