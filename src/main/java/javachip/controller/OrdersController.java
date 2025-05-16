/*
파일명 : OrdersController.java
파일설명 : 주문(Orders) 관련 요청 처리 컨트롤러
작성자 : 정여진, 김민하
기간 : 2025-05-02, 05-13
*/
package javachip.controller;

import javachip.dto.OrderItemDto;
import javachip.entity.Orders;
import javachip.repository.OrdersRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/orders")
@RequiredArgsConstructor
public class OrdersController {
    private final OrdersRepository ordersRepository;

    @GetMapping("/user/{userId}")
    public List<OrderSummaryResponse> getUserOrders(@PathVariable String userId) {
        List<Orders> ordersList = ordersRepository.findByUserId(userId);

        return ordersList.stream()
                .map(order -> new OrderSummaryResponse(
                        order.getId(),
                        order.getCreatedAt(),
                        order.getOrderItems().stream()
                                .map(OrderItemDto::fromEntity)
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }

    @Getter
    public static class OrderSummaryResponse {
        private final Long orderId;
        private final String orderDate;
        private final List<OrderItemDto> items;

        public OrderSummaryResponse(Long orderId, java.time.LocalDateTime orderDate, List<OrderItemDto> items) {
            this.orderId = orderId;
            this.orderDate = orderDate.toString();
            this.items = items;
        }

    }
}
