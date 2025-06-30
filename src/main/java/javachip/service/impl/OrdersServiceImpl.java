package javachip.service.impl;

import javachip.dto.order.consumer.OrderCreateRequest;
import javachip.dto.order.consumer.OrderCreateResponse;
import javachip.dto.order.consumer.OrderHistoryResponse;
import javachip.entity.order.OrderItem;
import javachip.entity.order.Orders;
import javachip.entity.product.Product;
import javachip.repository.order.OrderItemRepository;
import javachip.repository.order.OrderRepository;
import javachip.repository.product.ProductRepository;
import javachip.service.alarm.AlarmService;
import javachip.service.order.OrdersService;
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
    private final AlarmService alarmService;//

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

        OrderItem orderItem = request.toOrderItem(product, order);
        orderItemRepository.save(orderItem);

        // 알림 생성 (판매자에게) 김소망이 추가함
        alarmService.notifySellerOnOrder(orderItem);

        // 응답 반환
        return new OrderCreateResponse(orderItem);
    }

    @Override
    public List<OrderHistoryResponse> getUserOrders(String userId) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(OrderHistoryResponse::fromEntity)
                .collect(Collectors.toList());
    }
}
