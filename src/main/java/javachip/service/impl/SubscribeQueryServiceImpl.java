/*
 * 파일명 : SubscribeQueryServiceImpl.java
 * 파일설명 : 유저별 구독 주문 목록 조회 서비스 구현체
 * 작성자 : 정여진
 * 작성일 : 2025-05-26
 */

package javachip.service.impl;

import javachip.dto.subscription.SubscribeOrderResponseDto;
import javachip.entity.order.OrderItem;
import javachip.entity.subscribe.Subscription;
import javachip.repository.order.OrderItemRepository;
import javachip.service.subscribe.SubscribeQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubscribeQueryServiceImpl implements SubscribeQueryService {

    private final OrderItemRepository orderItemRepository;

    /**
     * userId에 해당하는 구독 주문(OrderItem)만 필터링하여 DTO로 반환
     */
    @Override
    public List<SubscribeOrderResponseDto> findSubscriptionsByUser(String userId) {
        List<OrderItem> items = orderItemRepository.findByUserIdAndIsSubscriptionTrue(userId);

        return items.stream()
                .filter(item -> item.getSubscription() != null) // NPE 방지
                .map(item -> {
                    Subscription sub = item.getSubscription();
                    return SubscribeOrderResponseDto.builder()
                            .id(sub.getId())
                            .productName(item.getProduct().getProductName())
                            .productId(item.getProduct().getId())
                            .startDate(item.getOrder().getCreatedAt().toLocalDate().toString())
                            .deliveryCycleType(item.getDeliveryCycleType())
                            .deliveryCycleValue(item.getDeliveryCycleValue())
                            .deliveryPeriodInMonths(item.getDeliveryPeriodInMonths())
                            .quantity(sub.getQuantity())
                            .orderItemId(item.getId())
                            .build();
                })
                .collect(Collectors.toList());
    }
}
