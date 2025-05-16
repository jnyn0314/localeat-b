package javachip.service;

import javachip.dto.groupbuy.*;
import javachip.entity.*;
import javachip.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupBuyService {

    private final ProductRepository productRepository;
    private final GroupBuyRepository groupBuyRepository;
    private final ConsumerRepository consumerRepository;
    private final ParticipantRepository participantRepository;

    @Transactional
    public GroupBuyCreateResponse createGroupBuy(GroupBuyCreateRequest request, String userId) {
        Consumer consumer = consumerRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 소비자입니다."));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("해당 상품이 존재하지 않습니다."));

        if (!product.getIsGroupBuy()) {
            throw new IllegalStateException("공동구매 상품이 아닙니다.");
        }

        // 1) GroupBuy 생성
        GroupBuy groupBuy = GroupBuy.builder()
                .product(product)
                .description(request.getDescription())
                .deadline(request.getDeadline())
                .status(GroupBuyStatus.RECRUITING)
                .time(request.getDeadline().atTime(23, 59))
                .partiCount(1)
                .payCount(0)
                .build();

        // 2) Participant 생성 및 연관 설정
        Participant participant = Participant.builder()
                .consumer(consumer)
                .groupBuy(groupBuy)
                .product(product)
                .quantity(request.getQuantity())
                .payment(false)
                .build();
        groupBuy.getParticipants().add(participant);

        // 3) 저장 (cascade = ALL 이라 participant도 같이 저장됨)
        groupBuyRepository.save(groupBuy);

        // 4) 응답 DTO 변환
        List<ParticipantResponse> participants = groupBuy.getParticipants().stream()
                .map(p -> ParticipantResponse.builder()
                        .consumerId(p.getConsumer().getUserId())
                        .quantity(p.getQuantity())
                        .payment(p.getPayment())
                        .build())
                .collect(Collectors.toList());

        return GroupBuyCreateResponse.builder()
                .groupBuyId(groupBuy.getId().longValue())
                .productId(product.getId())
                .description(groupBuy.getDescription())
                .deadline(groupBuy.getDeadline())
                .maxParticipants(product.getMaxParticipants())
                .currentParticipants(participants)
                .local(product.getLocal())
                .partiCount(groupBuy.getPartiCount())
                .payCount(groupBuy.getPayCount())
                .createdTime(groupBuy.getTime())
                .status(groupBuy.getStatus())
                .build();
    }

    @Transactional
    public void participateInGroupBuy(Long groupBuyId, String userId, int quantity) {
        Consumer consumer = consumerRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("해당 소비자가 존재하지 않습니다."));

        GroupBuy groupBuy = groupBuyRepository.findById(groupBuyId)
                .orElseThrow(() -> new RuntimeException("해당 공동구매가 존재하지 않습니다."));

        Optional<Participant> existing = participantRepository.findByConsumerAndGroupBuy(consumer, groupBuy);
        if (existing.isPresent()) {
            throw new IllegalStateException("이미 해당 공동구매에 참여하였습니다.");
        }

        Participant participant = Participant.builder()
                .consumer(consumer)
                .groupBuy(groupBuy)
                .product(groupBuy.getProduct())
                .quantity(quantity)
                .payment(false)
                .build();
        participantRepository.save(participant);

        groupBuy.setPartiCount(groupBuy.getPartiCount() + 1);
        groupBuyRepository.save(groupBuy);
    }

    @Transactional(readOnly = true)
    public GroupBuyDetailResponse getDetail(Long groupBuyId) {
        GroupBuy gb = groupBuyRepository.findById(groupBuyId)
                .orElseThrow(() -> new RuntimeException("해당 공동구매가 없습니다."));

        // 남은 시간 계산
        Duration d = Duration.between(LocalDateTime.now(), gb.getTime());
        long hours   = d.toHours();
        long minutes = d.toMinutesPart();
        long seconds = d.toSecondsPart();
        String remaining = String.format("%02d:%02d:%02d", hours, minutes, seconds);

        List<ParticipantDetailResponse> participants = gb.getParticipants().stream()
                .map(p -> ParticipantDetailResponse.builder()
                        .consumerId(p.getConsumer().getUserId())
                        .quantity(p.getQuantity())
                        .build())
                .collect(Collectors.toList());

        return GroupBuyDetailResponse.builder()
                .groupBuyId(gb.getId().longValue())
                .productId(gb.getProduct().getId())
                .productName(gb.getProduct().getProductName())
                .description(gb.getDescription())
                .deadline(gb.getDeadline())
                .remainingTime(remaining)
                .partiCount(gb.getPartiCount())
                .maxParticipants(gb.getProduct().getMaxParticipants())
                .status(gb.getStatus())
                .participants(participants)
                .build();
    }
}
