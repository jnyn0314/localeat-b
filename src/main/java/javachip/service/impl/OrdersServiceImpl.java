package javachip.service.impl;

import javachip.dto.order.OrderCreateRequest;
import javachip.dto.order.OrderCreateResponse;
import javachip.dto.order.OrderHistoryResponse;
import javachip.entity.OrderItem;
import javachip.entity.Orders;
import javachip.entity.Product;
import javachip.entity.GradeBOption;
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

        // 가격 계산 - 일반 구매 규칙 적용 (등급 할인만 적용)
        int originalPrice = product.getPrice();
        float gradeDiscountRate = product.getProductGrade() == GradeBOption.B ? product.getGradeDiscountRate() : 0f;
        int finalPrice = Math.round(originalPrice * (1 - gradeDiscountRate));

        // 주문 생성
        Orders order = Orders.builder()
                .createdAt(LocalDateTime.now())
                .build();
        orderRepository.save(order);

        // 주문 항목 생성
        OrderItem orderItem = request.toOrderItem(
                product,
                order,
                originalPrice,
                finalPrice,
                gradeDiscountRate,
                0f // 일반 구매는 구독 할인 없음
        );
        orderItemRepository.save(orderItem);

        // 주문 생성 응답 반환
        return new OrderCreateResponse(orderItem);
    }

    @Override
    public List<OrderHistoryResponse> getUserOrders(String userId) {
        return orderRepository.findByUserId(userId).stream()
                .map(OrderHistoryResponse::fromEntity)
                .collect(Collectors.toList());
    }
}
