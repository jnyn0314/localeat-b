package javachip.service.impl;

import javachip.dto.order.CartItemRequest;
import javachip.dto.order.CartItemResponse;
import javachip.dto.order.CartOrderRequest;
import javachip.dto.order.OrderCreateResponse;
import javachip.entity.*;
import javachip.repository.CartItemRepository;
import javachip.repository.CartRepository;
import javachip.repository.GeneralCartItemRepository;
import javachip.repository.OrderRepository;
import javachip.repository.ProductRepository;
import javachip.service.GeneralCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GeneralCartServiceImpl implements GeneralCartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final GeneralCartItemRepository generalCartItemRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

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
                .collect(Collectors.toList());
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
     * 일반구매 장바구니 주문 처리
     */
    @Override
    @Transactional
    public List<OrderCreateResponse> orderFromCart(String userId, CartOrderRequest request) {
        Cart cart = cartRepository.findByConsumer_UserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("장바구니가 존재하지 않습니다."));

        Orders order = Orders.builder()
                .userId(userId)
                .createdAt(LocalDateTime.now())
                .orderItems(new ArrayList<>())   // 빈 리스트 초기화
                .build();

        orderRepository.save(order); // 우선 저장 후 id 생성

        List<OrderItem> orderItems = request.getCartItemIds().stream()
                .map(cartItemId -> cartItemRepository.findById(cartItemId)
                        .orElseThrow(() -> new IllegalArgumentException("장바구니 항목이 존재하지 않습니다.")))
                .map(cartItem -> {
                    OrderItem orderItem = OrderItem.fromCartItem(cartItem);
                    orderItem.setOrder(order);  // 주문 객체 설정
                    return orderItem;
                })
                .collect(Collectors.toList());

        order.setOrderItems(orderItems);
        orderRepository.save(order);

        request.getCartItemIds().forEach(cartItemRepository::deleteById);

        return orderItems.stream()
                .map(OrderCreateResponse::fromEntity)
                .collect(Collectors.toList());
    }
}
