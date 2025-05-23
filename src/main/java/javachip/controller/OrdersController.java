package javachip.controller;

import javachip.dto.order.consumer.OrderCreateRequest;
import javachip.dto.order.consumer.OrderCreateResponse;
import javachip.dto.order.consumer.OrderHistoryResponse;
import javachip.service.OrdersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrdersController {

    private final OrdersService ordersService;

    // 주문 생성 요청 (1회 구매)
    @PostMapping("/single")
    public ResponseEntity<OrderCreateResponse> createSingleOrder(@RequestBody OrderCreateRequest request) {
        return ResponseEntity.ok(ordersService.createSingleOrder(request));
    }

    // 사용자 주문 내역 조회 (마이페이지)
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderHistoryResponse>> getUserOrders(@PathVariable String userId) {
        return ResponseEntity.ok(ordersService.getUserOrders(userId));
    }
}
