package javachip.service;

import jakarta.transaction.Transactional;
import javachip.dto.GroupBuyCreateRequest;
import javachip.dto.GroupBuyCreateResponse;
import javachip.entity.*;
import javachip.repository.ConsumerRepository;
import javachip.repository.GroupBuyRepository;
import javachip.repository.ParticipantRepository;
import javachip.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class GroupBuyService {
    private final ProductRepository productRepository;
    private final ConsumerRepository consumerRepository;
    private final GroupBuyRepository groupBuyRepository;
    private final ParticipantRepository participantRepository;

    @Transactional
    public GroupBuyCreateResponse createGroupBuy(GroupBuyCreateRequest request, String consumerId) {

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다."));

        if (product.getIs_group_buy() != GroupBuyOption.TRUE) {
            throw new IllegalStateException("해당 상품은 공동구매 대상이 아닙니다.");
        }

        Consumer consumer = consumerRepository.findById(consumerId)
                .orElseThrow(() -> new IllegalArgumentException("해당 소비자가 존재하지 않습니다."));

        GroupBuy groupBuy = GroupBuy.builder()
                .product(product)
                .description(request.getDescription())
                .deadline(request.getDeadline())
                .partiCount(1)  // 생성자 본인도 참가자
                .payCount(0)
                .status(GroupBuyStatus.RECRUITING)
                .participants(new ArrayList<>())
                .build();

        groupBuy = groupBuyRepository.save(groupBuy);

        // 참가자 생성
        Participant participant = Participant.builder()
                .consumer(consumer)
                .groupBuy(groupBuy)
                .quantity(request.getQuantity())
                .payment(false)
                .product(product)
                .build();

        participantRepository.save(participant);

        // GroupBuy에 추가
        groupBuy.getParticipants().add(participant);

        return GroupBuyCreateResponse.builder()
                .groupBuyId(groupBuy.getId())
                .productId(product.getId())
                .description(groupBuy.getDescription())
                .deadline(groupBuy.getDeadline())
                .maxParticipants(product.getMax_participants())
                .local(product.getLocal().name())
                .partiCount(groupBuy.getPartiCount())
                .payCount(groupBuy.getPayCount())
                .createdTime(LocalDateTime.now())
                .build();
    }
}
