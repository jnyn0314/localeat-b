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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupBuyService {

    private final ProductRepository productRepository;
    private final GroupBuyRepository groupBuyRepository;
    private final ConsumerRepository consumerRepository;
    private final ParticipantRepository participantRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final GroupBuyCartItemRepository groupBuyCartItemRepository;

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

        participantRepository.findByConsumerAndGroupBuy(consumer, groupBuy)
                .ifPresent(p -> { throw new IllegalStateException("이미 참여한 공동구매입니다."); });

        //Participant 저장
        Participant participant = Participant.builder()
                .consumer(consumer)
                .groupBuy(groupBuy)
                .product(groupBuy.getProduct())
                .quantity(quantity)
                .payment(false)
                .build();
        participantRepository.save(participant);
        groupBuy.getParticipants().add(participant);

        //partiCount 증가
        groupBuy.setPartiCount(groupBuy.getPartiCount() + 1);
        groupBuyRepository.save(groupBuy);

        //모집 완료 시 장바구니 이동
        if (groupBuy.getPartiCount().equals(groupBuy.getProduct().getMaxParticipants())) {
            moveParticipantsToCart(groupBuy);
            groupBuy.setStatus(GroupBuyStatus.PAYMENT_PENDING);
            groupBuyRepository.save(groupBuy);
        }
    }

    @Transactional(readOnly = true)
    public GroupBuyDetailResponse getDetail(Long groupBuyId) {
        GroupBuy gb = groupBuyRepository.findById(groupBuyId)
                .orElseThrow(() -> new RuntimeException("해당 공동구매가 없습니다."));

        // 남은 시간 계산
        Duration d = Duration.between(LocalDateTime.now(), gb.getTime());
        long hours = d.toHours();
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

    public List<GroupBuyListResponse> getGroupBuyListByProductId(Long productId) {
        LocalDateTime now = LocalDateTime.now();

        // 마감 시간이 아직 안 지난, 모집 중인 공구만 조회
        List<GroupBuy> groupBuys = groupBuyRepository
                .findByProduct_IdAndStatusAndTimeAfter(
                        productId,
                        GroupBuyStatus.RECRUITING,
                        now
                );

        return groupBuys.stream()
                .map(gb -> new GroupBuyListResponse(
                        gb.getId(),
                        gb.getProduct().getProductName(),
                        gb.getProduct().getLocal(),
                        gb.getProduct().getMaxParticipants(),
                        gb.getPartiCount(),
                        gb.getDescription(),
                        gb.getDeadline()
                ))
                .collect(Collectors.toList());
    }

    private void moveParticipantsToCart(GroupBuy gb) {
        for (Participant p : gb.getParticipants()) {
            // 1) Cart 생성(또는 조회)
            Cart cart = p.getConsumer().getCart();
            if (cart == null) {
                cart = new Cart();
                cartRepository.save(cart);
                p.getConsumer().setCart(cart);
                consumerRepository.save(p.getConsumer());
            }

            // 2) CartItem 생성
            CartItem base = CartItem.builder()
                    .product(gb.getProduct())
                    .quantity(p.getQuantity())
                    .isSelected(true)
                    .cart(cart)
                    .build();
            cartItemRepository.save(base);

            // 3) GroupBuyCartItem 생성 — cartItem 만 연결
            GroupBuyCartItem gci = GroupBuyCartItem.builder()
                    .cartItem(base)     // ← 핵심: CartItem 관계 설정
                    .groupBuy(gb)       // 공동구매 관계 설정
                    // paymentStatus 기본값(PENDING)과 addedAt/prePersist 처리됨
                    .build();
            groupBuyCartItemRepository.save(gci);
        }
        // (필요 시) 고객에게 “결제 대기 중” 알림 전송
    }

    //내가 참여한 공동구매 현황 조회
    @Transactional(readOnly = true)
    public List<MyGroupBuyStatusResponse> getMyParticipations(String userId) {
        List<Participant> parts = participantRepository.findAllByConsumerUserId(userId);

        LocalDateTime now = LocalDateTime.now();

        return parts.stream()
                .map(Participant::getGroupBuy)
                // ① 상태가 RECRUITING 또는 PAYMENT_PENDING 이고
                // ② 마감 시간이 현재(now) 이후인 것만 남긴다
                .filter(gb ->
                        (gb.getStatus() == GroupBuyStatus.RECRUITING
                                || gb.getStatus() == GroupBuyStatus.PAYMENT_PENDING)
                                && gb.getTime().isAfter(now)
                )
                .distinct()
                .map(gb -> MyGroupBuyStatusResponse.builder()
                        .groupBuyId(gb.getId().longValue())
                        .productId(gb.getProduct().getId())
                        .productName(gb.getProduct().getProductName())
                        .currentCount(gb.getPartiCount())
                        .maxParticipants(gb.getProduct().getMaxParticipants())
                        .deadline(gb.getDeadline())
                        .build())
                .collect(Collectors.toList());
    }
}
