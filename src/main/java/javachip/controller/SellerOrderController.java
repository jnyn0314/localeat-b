/*
파일명 : SellerOrderController.java
파일설명 : 판매자 상품의 주문 목록 조회 API / 주문 상태 변경 API
작성자 : 김민하
기간 : 2025-05.23.
*/
package javachip.controller;

import javachip.dto.order.seller.OrderStatusUpdateRequest;
import javachip.dto.order.seller.SellerOrderResponse;
import javachip.service.SellerOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seller/orders")
@RequiredArgsConstructor
public class SellerOrderController {

    private final SellerOrderService sellerOrderService;

    /**
     * 판매자 ID를 기준으로 상품에 연결된 주문 아이템 목록 조회
     */
    @GetMapping
    public ResponseEntity<List<SellerOrderResponse>> getOrdersBySeller(@RequestParam String sellerId) {
        List<SellerOrderResponse> orders = sellerOrderService.getOrdersBySeller(sellerId);
        return ResponseEntity.ok(orders);
    }

    /**
     * 특정 주문 아이템의 상태를 변경
     */
    @PutMapping("/{orderItemId}")
    public ResponseEntity<Void> updateOrderStatus(
            @PathVariable Long orderItemId,
            @RequestBody OrderStatusUpdateRequest request
    ) {
        sellerOrderService.updateOrderStatus(orderItemId, request.getStatus());
        return ResponseEntity.ok().build();
    }
}
