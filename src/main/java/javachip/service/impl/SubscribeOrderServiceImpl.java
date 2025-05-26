/*
 * 파일명 : SubscribeOrderServiceImpl.java
 * 파일설명 : 구독 주문 서비스 구현체. 상품 검증, 주문 객체 생성 및 저장 로직 포함
 * 작성자 : 정여진
 * 작성일 : 2025.05.26.
 */

package javachip.service.impl;

import javachip.dto.subscription.SubscribeOrderRequest;
import javachip.entity.Orders;
import javachip.entity.Product;
import javachip.repository.OrderRepository;
import javachip.repository.ProductRepository;
import javachip.service.SubscribeOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SubscribeOrderServiceImpl implements SubscribeOrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    /**
     * 구독 주문 생성
     * 1. 상품 조회
     * 2. 주문 엔티티 생성 (userId 포함)
     * 3. OrderItem 생성 후 주문에 추가
     * 4. 저장
     *
     * @param request  구독 주문 요청 DTO
     * @param userId   사용자 ID (헤더 또는 인증에서 전달됨)
     */
    @Override
    @Transactional
    public void createSubscribeOrder(SubscribeOrderRequest request, String userId) {

        System.out.println("[DEBUG] 서비스에 전달된 userId: " + userId);
        // 1. 상품 조회
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));

        // 2. 주문 생성
        Orders order = Orders.builder()
                .isSubscription(true)
                .createdAt(LocalDateTime.now())
                .userId(userId) //
                .build();

        // 3. 주문 항목 추가
        order.addOrderItem(request.toOrderItem(product, order));

        // 4. 주문 저장
        orderRepository.save(order);
    }
}
