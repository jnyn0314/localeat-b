/*
파일명 : OrderItemController.java
파일설명 : 주문 항목(OrderItem) 관련 요청 처리 컨트롤러
작성자 : 정여진
기간 : 2025-05-02
*/
package javachip.controller;

import javachip.entity.OrderItem;
import javachip.repository.OrderItemRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order-items")
public class OrderItemController {

    private final OrderItemRepository orderItemRepository;

    public OrderItemController(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    // 전체 주문 항목 조회
    @GetMapping
    public List<OrderItem> getAllOrderItems() {
        return orderItemRepository.findAll();
    }

    // 특정 ID 주문 항목 조회
    @GetMapping("/{id}")
    public OrderItem getOrderItemById(@PathVariable Long id) {
        return orderItemRepository.findById(id).orElse(null);
    }
}
