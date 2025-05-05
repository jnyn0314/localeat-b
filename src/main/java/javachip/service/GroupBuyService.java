package javachip.service;

import javachip.dto.GroupBuyCreateRequest;
import javachip.dto.GroupBuyCreateResponse;
import javachip.entity.*;
import javachip.repository.ConsumerRepository;
import javachip.repository.GroupBuyRepository;
import javachip.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupBuyService {

    private final ProductRepository productRepository;
    private final GroupBuyRepository groupBuyRepository;
    private final ConsumerRepository consumerRepository;

    public GroupBuyCreateResponse createGroupBuy(GroupBuyCreateRequest request, String userId) {
        // 1. 소비자 유효성 확인
        Consumer consumer = consumerRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 소비자입니다."));

        // 2. 상품 유효성 확인 및 공동구매 가능 여부 확인
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("해당 상품이 존재하지 않습니다."));

        if (product.getIs_group_buy() != GroupBuyOption.FALSE) {
            throw new RuntimeException("해당 상품은 공동구매가 불가능합니다.");
        }

        // 3. GroupBuy 생성
        GroupBuy groupBuy = GroupBuy.builder()
                .product(product)
                .description(request.getDescription())
                .deadline(request.getDeadline())
                .status(GroupBuyStatus.RECRUITING)
                .time(LocalDateTime.now().plusDays(1)) // 예시: 마감시간 후 24시간 내 결제
                .maxParticipants(product.getMax_participants())
                .local(product.getLocal())
                .partiCount(1)
                .payCount(0)
                .build();

        // 4. 첫 Participant 등록
        Participant participant = Participant.builder()
                .user(consumer)
                .product(product)
                .groupBuy(groupBuy)
                .quantity(request.getQuantity())
                .payment(false)
                .build();

        groupBuy.setParticipants(List.of(participant)); // 양방향 설정
        groupBuyRepository.save(groupBuy);

        // 5. 응답 반환
        return GroupBuyCreateResponse.builder()
                .groupBuyId(groupBuy.getGroupBuyId())
                .productId(product.getId())
                .description(groupBuy.getDescription())
                .deadline(groupBuy.getDeadline())
                .local(groupBuy.getLocal())
                .maxParticipants(groupBuy.getMaxParticipants())
                .currentParticipants(groupBuy.getPartiCount())
                .status(groupBuy.getStatus())
                .build();
    }
}
