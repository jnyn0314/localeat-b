package javachip.controller;

import javachip.dto.order.consumer.CartItemRequest;
import javachip.dto.order.consumer.CartItemResponse;
import javachip.dto.order.consumer.CartOrderRequest;
import javachip.dto.order.consumer.OrderCreateResponse;
import javachip.service.GeneralCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/general-cart")
@RequiredArgsConstructor
public class CartController {

    private final GeneralCartService cartService;

    /**
     * 일반 장바구니에 항목 추가
     */
    @PostMapping("/items")
    public ResponseEntity<Void> addToCart(
            @RequestHeader("userId") String userId,
            @RequestBody CartItemRequest request
    ) {
        cartService.addToCart(userId, request);
        return ResponseEntity.ok().build();
    }

    /**
     * 일반 장바구니 목록 조회
     */
    @GetMapping("/items")
    public ResponseEntity<List<CartItemResponse>> getCartItems(
            @RequestHeader("userId") String userId
    ) {
        List<CartItemResponse> cartItems = cartService.getCartItems(userId);
        return ResponseEntity.ok(cartItems);
    }

    /**
     * 일반 장바구니 항목 삭제
     */
    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<Void> deleteCartItem(
            @PathVariable Long cartItemId
    ) {
        cartService.deleteCartItem(cartItemId);
        return ResponseEntity.ok().build();
    }

    /**
     * 일반 장바구니 항목 주문 생성
     */
    @PostMapping("/order")
    public ResponseEntity<List<OrderCreateResponse>> orderFromCart(
            @RequestHeader("userId") String userId,
            @RequestBody CartOrderRequest request
    ) {
        List<OrderCreateResponse> responses = cartService.orderFromCart(userId, request);
        return ResponseEntity.ok(responses);
    }

}
