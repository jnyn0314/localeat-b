/*
파일명 : SellerOrderServiceImpl.java
파일설명 : 판매자 상품에 연결된 주문 목록 조회 / 주문 상태 수정
작성자 : 김민하
기간 : 2025-05.23.
*/
package javachip.service.impl;

import javachip.dto.order.seller.SellerOrderResponse;
import javachip.entity.OrderItem;
import javachip.entity.OrderStatus;
import javachip.repository.OrderItemRepository;
import javachip.service.SellerOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SellerOrderServiceImpl implements SellerOrderService {

    private final OrderItemRepository orderItemRepository;

    @Override
    public List<SellerOrderResponse> getOrdersBySeller(String sellerId) {
        List<OrderItem> orderItems = orderItemRepository.findByProductSellerUserId(sellerId);
        return orderItems.stream()
                .map(SellerOrderResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void updateOrderStatus(Long orderItemId, String status) {
        OrderItem item = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new IllegalArgumentException("해당 주문 아이템이 존재하지 않습니다."));

        item.setStatus(OrderStatus.valueOf(status));
        orderItemRepository.save(item);
    }
}
