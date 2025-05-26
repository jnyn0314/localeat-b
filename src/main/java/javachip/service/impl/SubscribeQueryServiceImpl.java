/*
 * 파일명 : SubscribeQueryServiceImpl.java
 * 파일설명 : 유저별 구독 주문 목록 조회 서비스 구현체
 * 작성자 : 정여진
 * 작성일 : 2025-05-26
 */

package javachip.service.impl;

import javachip.dto.subscription.SubscribeOrderResponseDto;
import javachip.entity.OrderItem;
import javachip.repository.OrderItemRepository;
import javachip.service.SubscribeQueryService;
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
                .map(item -> SubscribeOrderResponseDto.builder()
                        .productName(item.getProduct().getProductName())
                        .startDate(item.getOrder().getCreatedAt().toLocalDate().toString())
                        .deliveryCycleType(item.getDeliveryCycleType())
                        .deliveryCycleValue(item.getDeliveryCycleValue())
                        .deliveryPeriodInMonths(item.getDeliveryPeriodInMonths())
                        .quantity(item.getQuantity())
                        .build())
                .collect(Collectors.toList());
    }
}