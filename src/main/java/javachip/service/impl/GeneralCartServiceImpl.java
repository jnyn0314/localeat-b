package javachip.service.impl;

import javachip.dto.order.consumer.CartItemRequest;
import javachip.dto.order.consumer.CartItemResponse;
import javachip.dto.order.consumer.CartOrderRequest;
import javachip.dto.order.consumer.OrderCreateResponse;
import javachip.dto.order.consumer.OrderCreateRequest;
import javachip.entity.*;
import javachip.repository.*;
import javachip.service.AlarmService;
import javachip.service.GeneralCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class GeneralCartServiceImpl implements GeneralCartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final GeneralCartItemRepository generalCartItemRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final AlarmService alarmService;//김소망이 추가함

    /**
     * 일반구매 장바구니 항목 추가
     */
    @Override
    @Transactional
    public void addToCart(String userId, CartItemRequest request) {
        Consumer newConsumer = new Consumer();
        newConsumer.setUserId(userId);

        Cart cart = cartRepository.findByConsumer_UserId(userId)
                .orElseGet(() -> cartRepository.save(new Cart(newConsumer)));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));

        cartItemRepository.findByCartIdAndProductId(cart.getId(), product.getId()).stream()
                .filter(item -> item.getGeneralCartItem() != null)  // 일반구매만 확인
                .findFirst()
                .ifPresentOrElse(
                        existingItem -> {
                            existingItem.setQuantity(existingItem.getQuantity() + request.getQuantity());
                            cartItemRepository.save(existingItem);
                        },
                        () -> {
                            CartItem cartItem = request.toEntity(cart, product);
                            cartItemRepository.save(cartItem);

                            GeneralCartItem generalCartItem = GeneralCartItem.builder()
                                    .cartItem(cartItem)
                                    .build();
                            generalCartItemRepository.save(generalCartItem);
                        }
                );
    }

    /**
     * 일반구매 장바구니 목록 조회
     */
    @Override
    @Transactional(readOnly = true)
    public List<CartItemResponse> getCartItems(String userId) {
        Cart cart = cartRepository.findByConsumer_UserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("장바구니가 존재하지 않습니다."));

        return cartItemRepository.findAllByCartId(cart.getId()).stream()
                .filter(item -> item.getGeneralCartItem() != null)  // 일반구매만 필터링
                .map(CartItemResponse::fromEntity)
                .collect(toList());
    }

    /**
     * 일반구매 장바구니 항목 삭제
     */
    @Override
    @Transactional
    public void deleteCartItem(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("장바구니 항목이 존재하지 않습니다."));
        cartItemRepository.delete(cartItem);
    }

    /**
     * 일반구매 장바구니 주문 처리 이 부분 알림 기능 때문에 김소망이 수정 및 추가 함 달라졌다고 의문ㄴㄴ
     */
    @Override
    @Transactional
    public List<OrderCreateResponse> orderFromCart(String userId, CartOrderRequest request) {
        Orders order = Orders.builder()
                .userId(userId)
                .createdAt(LocalDateTime.now())
                .orderItems(new ArrayList<>())
                .build();
        orderRepository.save(order);

        List<OrderItem> orderItems = request.getCartItemIds().stream()
                .map(cartItemId -> cartItemRepository.findById(cartItemId)
                        .orElseThrow(() -> new IllegalArgumentException("장바구니 항목이 존재하지 않습니다.")))
                .map(cartItem -> {
                    OrderItem item = OrderCreateRequest.fromCartItem(cartItem);
                    item.setOrder(order);
                    return item;
                })
                .toList();

        orderItems.forEach(orderItemRepository::save);

        // 알림 생성 (판매자에게)
        orderItems.forEach(orderItem -> alarmService.notifySellerOnOrder(orderItem));

        request.getCartItemIds().forEach(cartItemRepository::deleteById);

        return orderItems.stream()
                .map(OrderCreateResponse::fromEntity)
                .toList();
    }
}
