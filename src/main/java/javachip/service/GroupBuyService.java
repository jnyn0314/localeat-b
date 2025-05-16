package javachip.service;

import javachip.dto.groupbuy.GroupBuyCreateRequest;
import javachip.dto.groupbuy.GroupBuyCreateResponse;
import javachip.dto.groupbuy.ParticipantResponse;
import javachip.entity.*;
import javachip.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupBuyService {

    private final ProductRepository productRepository;
    private final GroupBuyRepository groupBuyRepository;
    private final ConsumerRepository consumerRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    @Transactional
    public GroupBuyCreateResponse createGroupBuy(GroupBuyCreateRequest request, String userId) {
        // 1. 소비자 유효성 확인
        Consumer consumer = consumerRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 소비자입니다."));

        // 2. 상품 유효성 확인 및 공동구매 가능 여부 확인
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("해당 상품이 존재하지 않습니다."));

        if (Boolean.FALSE.equals(product.getIsGroupBuy())) {
            throw new IllegalStateException("공동구매 상품이 아닙니다.");

        }

        // 1. GroupBuy 생성
        GroupBuy groupBuy = GroupBuy.builder()
                .product(product)
                .description(request.getDescription())
                .deadline(request.getDeadline())
                .status(GroupBuyStatus.RECRUITING)
                .time(request.getDeadline().atTime(23, 59))
                .partiCount(1)
                .payCount(0)
                .participants(new ArrayList<>()) // 이건 있어야 함
                .build();

// 2. Participant 생성
        Participant participant = Participant.builder()
                .consumer(consumer)
                .product(product)
                .groupBuy(groupBuy)
                .quantity(request.getQuantity())
                .payment(false)
                .build();

// 3. 양방향 관계 명시적 설정
        groupBuy.getParticipants().add(participant); // ✅ 꼭 필요
// participant.setGroupBuy(groupBuy); // 이미 builder에 있음

// 4. 저장
        groupBuyRepository.save(groupBuy); // CascadeType.ALL로 participant도 같이 저장됨

        // 5. 응답 반환
        List<ParticipantResponse> participantResponses = groupBuy.getParticipants().stream()
                .map(p -> ParticipantResponse.builder()
                        .consumerId(p.getConsumer().getUserId())
                        .quantity(p.getQuantity())
                        .payment(p.getPayment())
                        .build())
                .collect(Collectors.toList());

        return GroupBuyCreateResponse.builder()
                .groupBuyId(Long.valueOf(groupBuy.getId()))
                .productId(product.getId())
                .description(groupBuy.getDescription())
                .deadline(groupBuy.getDeadline())
                .maxParticipants(product.getMaxParticipants())
                .currentParticipants(participantResponses)
                .local(product.getLocal())
                .partiCount(groupBuy.getPartiCount())
                .payCount(groupBuy.getPayCount())
                .createdTime(groupBuy.getTime())
                .status(groupBuy.getStatus())
                .build();

    }
}
