/*
파일명: SubscribeCartServiceImpl.java
설명: 구독 장바구니 서비스 구현체.
작성자 : 정여진
작성일 : 2025.05.29.
*/

package javachip.service.impl;

import javachip.dto.subscription.SubscribeCartItemRequest;
import javachip.dto.subscription.SubscribeCartItemResponse;
import javachip.entity.cart.Cart;
import javachip.entity.cart.Consumer;
import javachip.entity.product.Product;
import javachip.entity.cart.SubscribeCartItem;
import javachip.repository.cart.CartRepository;
import javachip.repository.user.ConsumerRepository;
import javachip.repository.product.ProductRepository;
import javachip.repository.subscribe.SubscribeCartItemRepository;
import javachip.service.cart.SubscribeCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscribeCartServiceImpl implements SubscribeCartService {

    private final SubscribeCartItemRepository subscribeCartItemRepository;
    private final ConsumerRepository consumerRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;

    @Override
    public List<SubscribeCartItemResponse> getItems(String consumerId) {
        return subscribeCartItemRepository.findByCart_Consumer_Id(consumerId)
                .stream()
                .map(SubscribeCartItemResponse::fromEntity)
                .toList();
    }

    @Override
    public void deleteItem(Long cartItemId) {
        subscribeCartItemRepository.deleteById(cartItemId);
    }

    @Override
    public void toggleItem(Long cartItemId, boolean selected) {
        SubscribeCartItem item = subscribeCartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("해당 항목이 존재하지 않습니다."));
        item.setSelected(selected);
        subscribeCartItemRepository.save(item);
    }

    @Override
    public void toggleAll(String consumerId, boolean selected) {
        List<SubscribeCartItem> items = subscribeCartItemRepository.findByCart_Consumer_Id(consumerId);
        for (SubscribeCartItem item : items) {
            item.setSelected(selected);
        }
        subscribeCartItemRepository.saveAll(items);
    }

    @Override
    @Transactional
    public void addItem(String userId, SubscribeCartItemRequest request) {
        // 1. 사용자 → 장바구니 조회
        Consumer consumer = consumerRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        Cart cart = consumer.getCart();
        if (cart == null) {
            cart = new Cart(consumer);
            cartRepository.save(cart);
            consumer.setCart(cart);
        }

        // 2. 상품 조회
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));

        // 3. SubscribeCartItem 생성
        SubscribeCartItem item = (SubscribeCartItem) SubscribeCartItem.builder()
                .cart(cart)
                .product(product)
                .quantity(request.getQuantity())
                //.price(request.getPrice())
                //.deliveryCycle(request.getCycleType() + "-" + request.getCycleValue()) // 예: "WEEKLY-1"
                //.selected(true)
                .build();

        // 4. 저장
        subscribeCartItemRepository.save(item);
    }
}
