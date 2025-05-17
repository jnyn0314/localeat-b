package javachip.controller;

import javachip.dto.groupbuy.CartItemPayRequest;
import javachip.dto.groupbuy.GroupBuyCartItemResponse;
import javachip.entity.GroupBuy;
import javachip.entity.GroupBuyCartItem;
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
        List<GroupBuyCartItem> items =
                groupBuyCartItemRepository.findByCartItem_Cart_Consumer_UserId(userId);

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
        // Repository 필드를 사용
        GroupBuyCartItem item = groupBuyCartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("장바구니 아이템이 없습니다."));

        PaymentStatus status = PaymentStatus.valueOf(req.getPaymentStatus());

        // 결제 상태 업데이트
        item.setPaymentStatus(status);
        groupBuyCartItemRepository.save(item);

        // 결제 완료 시 GroupBuy.payCount 증가
        if (status == PaymentStatus.COMPLETED) {
            GroupBuy gb = item.getGroupBuy();
            gb.setPayCount(gb.getPayCount() + 1);
            groupBuyRepository.save(gb);
        }

        return ResponseEntity.ok().build();
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
        // 권한 체크 생략…
        groupBuyCartItemRepository.delete(item);
        return ResponseEntity.noContent().build();
    }
}