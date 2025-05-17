package javachip.service.impl;

import javachip.dto.order.OrderCreateRequest;
import javachip.dto.order.OrderCreateResponse;
import javachip.dto.order.OrderHistoryResponse;
import javachip.entity.OrderItem;
import javachip.entity.Orders;
import javachip.entity.Product;
import javachip.repository.OrderItemRepository;
import javachip.repository.OrderRepository;
import javachip.repository.ProductRepository;
import javachip.service.OrdersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrdersServiceImpl implements OrdersService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;

    @Override
    public OrderCreateResponse createSingleOrder(OrderCreateRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

        // 주문 생성
        Orders order = Orders.builder()
                .userId(request.getUserId())
                .createdAt(LocalDateTime.now())
                .build();
        orderRepository.save(order);

        // 프론트 최종 가격 그대로 저장
        OrderItem orderItem = request.toOrderItem(product, order);
        orderItemRepository.save(orderItem);

        // 응답 반환
        return new OrderCreateResponse(orderItem);
    }

    @Override
    public List<OrderHistoryResponse> getUserOrders(String userId) {
        return orderRepository.findByUserId(userId).stream()
                .map(OrderHistoryResponse::fromEntity)
                .collect(Collectors.toList());
    }
}
