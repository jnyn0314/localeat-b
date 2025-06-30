package javachip.controller.cart;

import javachip.dto.groupbuy.CartItemPayRequest;
import javachip.dto.groupbuy.GroupBuyCartItemResponse;
import javachip.entity.cart.CartItem;
import javachip.entity.cart.GroupBuyCartItem;
import javachip.entity.groupbuy.GroupBuy;
import javachip.entity.groupbuy.GroupBuyStatus;
import javachip.entity.groupbuy.Participant;
import javachip.entity.order.OrderItem;
import javachip.entity.order.OrderStatus;
import javachip.entity.order.Orders;
import javachip.entity.product.PaymentStatus;
import javachip.repository.groupbuy.GroupBuyCartItemRepository;
import javachip.repository.groupbuy.GroupBuyRepository;
import javachip.repository.order.OrderRepository;
import javachip.service.alarm.AlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/group-buy-cart")
@RequiredArgsConstructor
public class GroupBuyCartController {

    private final GroupBuyCartItemRepository cartItemRepo;
    private final GroupBuyRepository       groupBuyRepo;
    private final OrderRepository          orderRepo;
    private final AlarmService alarmService;

    /**내 장바구니(공구) 전체 조회 */
    @GetMapping
    public ResponseEntity<List<GroupBuyCartItemResponse>> getMyCart(
            @RequestHeader("X-USER-ID") String userId
    ) {
        List<GroupBuyCartItem> items = cartItemRepo
                .findByCartItem_Cart_Consumer_UserIdAndPaymentStatus(
                        userId, PaymentStatus.PENDING
                );
        var dto = items.stream()
                .map(GroupBuyCartItemResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dto);
    }

    /** 단건 결제 처리 */
    @PostMapping("/{cartItemId}/pay")
    public ResponseEntity<Void> payCartItem(
            @PathVariable Long cartItemId,
            @RequestHeader("X-USER-ID") String userId,
            @RequestBody CartItemPayRequest req
    ) {
        GroupBuyCartItem item = cartItemRepo.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("장바구니 아이템이 없습니다."));
        if (item.getPaymentStatus() != PaymentStatus.PENDING) {
            throw new RuntimeException("이미 처리된 주문입니다.");
        }
        var status = PaymentStatus.valueOf(req.getPaymentStatus());
        item.setPaymentStatus(status);
        cartItemRepo.save(item);

        if (status == PaymentStatus.COMPLETED) {
            GroupBuy gb = item.getGroupBuy();
            gb.setPayCount(gb.getPayCount() + 1);
            if (gb.getPayCount().equals(gb.getPartiCount())) {
                gb.setStatus(GroupBuyStatus.SUCCESS);
            }
            groupBuyRepo.save(gb);


        }
        return ResponseEntity.ok().build();
    }

    /**전체 주문(장바구니 -> 주문) 처리*/
    @PostMapping("/order")
    @Transactional
    public ResponseEntity<Void> createOrder(
            @RequestHeader("X-USER-ID") String userId,
            @RequestBody List<Long> cartItemIds
    ) {
        //장바구니 아이템 조회
        List<GroupBuyCartItem> cartItems = cartItemRepo.findAllById(cartItemIds);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("선택된 장바구니가 없습니다.");
        }

        //Order, OrderItem 생성
        Orders order = Orders.builder()
                .createdAt(LocalDateTime.now())
                .userId(userId)
                .build();

        List<OrderItem> orderItems = cartItems.stream().map(ci -> {
            CartItem base = ci.getCartItem();
            int originalPrice = base.getProduct().getPrice();
            int discountedPrice = (int) Math.floor(originalPrice * 0.9); // 10% 할인

            return OrderItem.builder()
                    .quantity(base.getQuantity())
                    .status(OrderStatus.PAID)
                    .isSubscription(false)
                    .isGroupBuy(true)
                    .product(base.getProduct())
                    .order(order)
                    .userId(userId)
                    .isReviewed(false)
                    .price(discountedPrice) //할인된 가격
                    .build();
        }).collect(Collectors.toList());
        order.setOrderItems(orderItems);
        orderRepo.save(order);// 주문 저장

        // 일반 주문만 알림 전송
        orderItems.forEach(orderItem -> {
            if (!orderItem.isGroupBuy()) {
                alarmService.notifySellerOnOrder(orderItem);
            }
        });

        //payCount 증감 집계: key=그룹바이ID, value=증가할 카운트
        Map<Long,Integer> increments = new HashMap<>();
        for (var ci : cartItems) {
            long gbId = ci.getGroupBuy().getId().longValue();
            increments.put(gbId, increments.getOrDefault(gbId, 0) + 1);
        }

        //각 GroupBuy에 반영
        for (var entry : increments.entrySet()) {
            GroupBuy gb = groupBuyRepo.findById(entry.getKey())
                    .orElseThrow();

            gb.setPayCount(gb.getPayCount() + entry.getValue());

            if (gb.getPayCount().equals(gb.getPartiCount())) {
                gb.setStatus(GroupBuyStatus.SUCCESS);
                groupBuyRepo.save(gb);

                // ✅ 판매자 알림
                for (OrderItem orderItem : orderItems) {
                    if (orderItem.isGroupBuy() &&
                            orderItem.getProduct().equals(gb.getProduct())) {
                        alarmService.notifySellerOnGroupBuyOrderSuccess(orderItem, gb);
                    }
                }

                // ✅ 구매자 알림 (성공 참여자 전체)
                for (Participant p : gb.getParticipants()) {
                    alarmService.notifyGroupBuySuccessToBuyer(p.getConsumer(), gb);
                }

            } else {
                groupBuyRepo.save(gb); // 성공 아닐 땐 그냥 저장만
            }
        }


        //cartItem 상태도 모두 COMPLETED로
        for (var ci : cartItems) {
            ci.setPaymentStatus(PaymentStatus.COMPLETED);
            cartItemRepo.save(ci);
        }

        return ResponseEntity.ok().build();
    }

    /** cartItem 직접 삭제 */
    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<Void> deleteCartItem(
            @PathVariable Long cartItemId,
            @RequestHeader("X-USER-ID") String userId
    ) {
        GroupBuyCartItem item = cartItemRepo.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("장바구니 아이템이 없습니다."));
        cartItemRepo.delete(item);
        return ResponseEntity.noContent().build();
    }
}
