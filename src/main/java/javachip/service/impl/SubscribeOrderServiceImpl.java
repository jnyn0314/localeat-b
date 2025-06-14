/*
 * 파일명 : SubscribeOrderServiceImpl.java
 * 파일설명 : 구독 주문 서비스 구현체. 상품 검증, 주문 객체 생성 및 저장 로직 포함
 * 작성자 : 정여진
 * 작성일 : 2025.05.26.
 */

package javachip.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import javachip.dto.subscription.SubscribeOrderRequest;
import javachip.dto.subscription.SubscribeOrderResponseDto;
import javachip.dto.subscription.SubscribeUpdateRequest;
import javachip.entity.*;
import javachip.repository.OrderRepository;
import javachip.repository.ProductRepository;
import javachip.repository.SubscriptionRepository;
import javachip.service.AlarmService;
import javachip.service.SubscribeOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubscribeOrderServiceImpl implements SubscribeOrderService {
    @PersistenceContext
    private EntityManager entityManager;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final AlarmService alarmService;
    private final SubscriptionRepository subscriptionRepository;
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
                .userId(userId)
                .build();

        // 3. 주문 항목 생성 및 주문에 추가
        OrderItem orderItem = request.toOrderItem(product, order);
        order.addOrderItem(orderItem);

        // 4. 주문 저장 (cascade 설정되어 있으면 orderItem도 같이 저장됨)
        orderRepository.save(order);
        entityManager.flush(); // ← 강제 flush로 ID 생성

        System.out.println("생성된 OrderItem ID: " + orderItem.getId());
        System.out.println("생성된 OrderItem ID: " + orderItem.getId());

        // 5. Subscription 엔티티 생성 및 저장
        Subscription subscription = Subscription.builder()
                .orderItem(orderItem)
                .subscribedAt(LocalDate.from(LocalDateTime.now())) // 또는 order.getCreatedAt()
                .deliveryCycle(request.getDeliveryCycleTypeEnum()) // enum으로 변환 필요
                .deliveryCycleValue(request.getDeliveryCycleValue())
                .quantity(request.getQuantity())
                .nextDeliveryDate(LocalDate.now().plusDays(3))
                .periodInMonths(request.getDeliveryPeriodInMonths())
                .userId(userId)
                .build();

        subscriptionRepository.save(subscription);

        // 6. 알림 전송
        alarmService.notifySellerOnOrder(orderItem);
    }

    @Transactional
    @Override
    public void updateSubscription(Long id, SubscribeUpdateRequest request) {
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 구독 ID입니다: " + id));
        System.out.println("🔄 [Update] 요청 ID = " + id);
        System.out.println("🔄 [Update] request = " + request.getDeliveryCycleType() + ", " + request.getDeliveryCycleValue());
        System.out.println("🔄 [Update] before = " + subscription.getDeliveryCycle() + ", " + subscription.getDeliveryCycleValue());

        subscription.setQuantity(request.getQuantity());
        // Construct the enum value by combining type and value
        try {
            String enumValue = request.getDeliveryCycleType() + "_" + request.getDeliveryCycleValue();
            subscription.setDeliveryCycle(DeliveryCycle.valueOf(enumValue));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("올바르지 않은 배송 주기 값입니다: " + request.getDeliveryCycleType() + "_" + request.getDeliveryCycleValue());
        }
        subscription.setDeliveryCycleValue(request.getDeliveryCycleValue());
        System.out.println("🔄 [Update] after = " + subscription.getDeliveryCycle() + ", " + subscription.getDeliveryCycleValue());

        subscriptionRepository.save(subscription); // 또는 @Transactional이면 생략 가능

        entityManager.flush();
        entityManager.clear();
    }

    @Transactional
    @Override
    public List<SubscribeOrderResponseDto> getSubscriptions(String userId) {
        return subscriptionRepository.findByUserId(userId)
                .stream()
                .map(subscription -> {
                    System.out.println("🔍 응답 매핑 중: id=" + subscription.getId()
                            + ", quantity=" + subscription.getQuantity()
                            + ", cycle=" + subscription.getDeliveryCycle());

                    return SubscribeOrderResponseDto.builder()
                            .id(subscription.getId())
                            .productName(subscription.getOrderItem().getProduct().getProductName())
                            .startDate(subscription.getSubscribedAt().toString())
                            .deliveryCycleType(subscription.getDeliveryCycle().name().split("_")[0]) // 예: WEEKLY
                            .deliveryCycleValue(subscription.getDeliveryCycleValue()) // 예: 1
                            .quantity(subscription.getQuantity())
                            .deliveryPeriodInMonths(subscription.getPeriodInMonths())
                            .build();
                })
                .collect(Collectors.toList());
    }
}
