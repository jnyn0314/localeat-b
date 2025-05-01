/*
파일명 : OrdersController.java
파일설명 : 주문(Orders) 관련 요청 처리 컨트롤러
작성자 : 정여진
기간 : 2025-05-02
*/
package javachip.controller;

import javachip.entity.Orders;
import javachip.repository.OrdersRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrdersController {
    private final OrdersRepository ordersRepository;

    public OrdersController(OrdersRepository ordersRepository) {
        this.ordersRepository = ordersRepository;
    }

    @GetMapping
    public List<Orders> getAllOrders() {
        return ordersRepository.findAll();
    }
}
