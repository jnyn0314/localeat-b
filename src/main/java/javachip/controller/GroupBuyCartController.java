package javachip.controller;

import javachip.dto.groupbuy.CartItemPayRequest;
import javachip.dto.groupbuy.GroupBuyCartItemResponse;
import javachip.entity.*;
import javachip.repository.GroupBuyCartItemRepository;
import javachip.repository.GroupBuyRepository;
import javachip.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class GroupBuyCartController {

    private final GroupBuyCartItemRepository groupBuyCartItemRepository;
    private final GroupBuyRepository groupBuyRepository;
    private final OrderRepository ordersRepository;

    /**
     * 1. 내 장바구니(공구) 전체 조회
     */
    @GetMapping
    public ResponseEntity<List<GroupBuyCartItemResponse>> getMyCart(
            @RequestHeader("X-USER-ID") String userId
    ) {
        // PENDING 상태인 아이템만 조회
        List<GroupBuyCartItem> items =
                groupBuyCartItemRepository.findByCartItem_Cart_Consumer_UserIdAndPaymentStatus(userId, PaymentStatus.PENDING);

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

// 1. 현재 상태가 PENDING이 아니면 예외 발생
        if (item.getPaymentStatus() != PaymentStatus.PENDING) {
            throw new RuntimeException("이미 처리된 주문입니다.");
        }

        try {
            // 2. 실제 결제 처리 로직 (예: PG사 연동)
            PaymentStatus status = PaymentStatus.valueOf(req.getPaymentStatus());

            // 3. 결제 결과에 따른 상태 업데이트
            item.setPaymentStatus(status);
            groupBuyCartItemRepository.save(item);

            // 4. 결제 완료 시에만 GroupBuy.payCount 증가
            if (status == PaymentStatus.COMPLETED) {
                GroupBuy gb = item.getGroupBuy();
                gb.setPayCount(gb.getPayCount() + 1);
                groupBuyRepository.save(gb);
            }

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            // 5. 결제 실패 시 EXPIRED 상태로 변경
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
        // 권한 체크 생략…
        groupBuyCartItemRepository.delete(item);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/order")
    public ResponseEntity<Void> createOrder(
            @RequestHeader("X-USER-ID") String userId,
            @RequestBody List<Long> cartItemIds // 프론트에서 체크한 cartItemId 리스트
    ) {
        // 1. 장바구니 아이템 조회
        List<GroupBuyCartItem> cartItems = groupBuyCartItemRepository.findAllById(cartItemIds);

        // 2. Orders 생성
        Orders order = Orders.builder()
                .createdAt(LocalDateTime.now())
                .userId(userId)
                .build();

        // 3. OrderItem 생성
        List<OrderItem> orderItems = cartItems.stream().map(cartItem -> {
            CartItem baseCartItem = cartItem.getCartItem(); // cartItem 필드 접근
            OrderItem orderItem = OrderItem.builder()
                    .quantity(baseCartItem.getQuantity())
                    .status(OrderStatus.PAID) // 결제완료 상태로
                    .isSubscription(false) // 공동구매는 false
                    .isGroupBuy(true)
                    .product(baseCartItem.getProduct())
                    .order(order)
                    .userId(userId)
                    .isReviewed(false)
                    .price(baseCartItem.getProduct().getPrice())
                    .build();
            return orderItem;
        }).collect(Collectors.toList());

        order.setOrderItems(orderItems);

        // 4. 주문 저장 (Order, OrderItem 모두 저장됨)
        ordersRepository.save(order);

        // 5. 장바구니 아이템 결제완료 처리
        for (GroupBuyCartItem cartItem : cartItems) {
            cartItem.setPaymentStatus(PaymentStatus.COMPLETED);
            groupBuyCartItemRepository.save(cartItem);
        }

        return ResponseEntity.ok().build();
    }
}