package javachip.controller;

import javachip.dto.groupbuy.CartItemPayRequest;
import javachip.dto.groupbuy.GroupBuyCartItemResponse;
import javachip.entity.GroupBuy;
import javachip.entity.GroupBuyCartItem;
import javachip.entity.GroupBuyStatus;
import javachip.entity.PaymentStatus;
import javachip.repository.GroupBuyCartItemRepository;
import javachip.repository.GroupBuyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class GroupBuyCartController {

    private final GroupBuyCartItemRepository groupBuyCartItemRepository;
    private final GroupBuyRepository groupBuyRepository;

    /**
     * 1. 내 장바구니(공구) 전체 조회
     */
    @GetMapping
    public ResponseEntity<List<GroupBuyCartItemResponse>> getMyCart(
            @RequestHeader("X-USER-ID") String userId
    ) {
        // PENDING 상태인 아이템만 조회
        List<GroupBuyCartItem> items =
                groupBuyCartItemRepository
                        .findByCartItem_Cart_Consumer_UserIdAndPaymentStatus(
                                userId, PaymentStatus.PENDING
                        );

        List<GroupBuyCartItemResponse> dto = items.stream()
                .map(GroupBuyCartItemResponse::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dto);
    }

    /**
     * 2. 해당 cartItem 결제 처리
     */
    @PostMapping("/{cartItemId}/pay")
    public ResponseEntity<Void> payCartItem(
            @PathVariable Long cartItemId,
            @RequestHeader("X-USER-ID") String userId,
            @RequestBody CartItemPayRequest req
    ) {
        GroupBuyCartItem item = groupBuyCartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("장바구니 아이템이 없습니다."));

        // 1) 이미 처리된 건인지 확인
        if (item.getPaymentStatus() != PaymentStatus.PENDING) {
            throw new RuntimeException("이미 처리된 주문입니다.");
        }

        try {
            // 2) 결제 상태 값(enum) 변환
            PaymentStatus status = PaymentStatus.valueOf(req.getPaymentStatus());

            // 3) 결제 결과 반영
            item.setPaymentStatus(status);
            groupBuyCartItemRepository.save(item);

            // 4) 결제 완료 시에만 payCount++ 및 SUCCESS 전환
            if (status == PaymentStatus.COMPLETED) {
                GroupBuy gb = item.getGroupBuy();
                gb.setPayCount(gb.getPayCount() + 1);
                if (gb.getPayCount().equals(gb.getPartiCount())) {
                    gb.setStatus(GroupBuyStatus.SUCCESS);
                }
                groupBuyRepository.save(gb);
            }

            return ResponseEntity.ok().build();

        } catch (Exception e) {
            // 5) 내부 오류 시 EXPIRED 상태로
            item.setPaymentStatus(PaymentStatus.EXPIRED);
            groupBuyCartItemRepository.save(item);
            throw new RuntimeException("결제 처리 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    /**
     * 3. (선택) cartItem 직접 제거
     */
    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<Void> deleteCartItem(
            @PathVariable Long cartItemId,
            @RequestHeader("X-USER-ID") String userId
    ) {
        GroupBuyCartItem item = groupBuyCartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("장바구니 아이템이 없습니다."));
        groupBuyCartItemRepository.delete(item);
        return ResponseEntity.noContent().build();
    }
}
