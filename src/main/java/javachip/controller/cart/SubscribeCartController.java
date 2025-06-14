package javachip.controller.cart;

import javachip.dto.subscription.SubscribeCartItemRequest;
import javachip.dto.subscription.SubscribeCartItemResponse;
import javachip.service.cart.SubscribeCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart/subscribe")
public class SubscribeCartController {

    private final SubscribeCartService subscribeCartService;

    @PostMapping("/items")
    public ResponseEntity<?> addToCart(@RequestHeader("userId") String userId, @RequestBody SubscribeCartItemRequest dto) {
        System.out.println("✅ POST /items 진입 성공");
        subscribeCartService.addItem(userId, dto);
        return ResponseEntity.ok().build();
    }

    // 장바구니 목록 조회
    @GetMapping("/{userId}")
    public List<SubscribeCartItemResponse> getItems(@PathVariable String userId) {
        return subscribeCartService.getItems(userId);
    }

    // 장바구니 항목 삭제
    @DeleteMapping("/{cartItemId}")
    public void deleteItem(@PathVariable Long cartItemId) {
        subscribeCartService.deleteItem(cartItemId);
    }

    // 개별 항목 선택 여부 토글
    @PostMapping("/{cartItemId}/toggle")
    public void toggleItem(@PathVariable Long cartItemId, @RequestParam boolean selected) {
        subscribeCartService.toggleItem(cartItemId, selected);
    }

    // 전체 선택/해제
    @PostMapping("/toggle-all")
    public void toggleAll(@RequestParam String userId, @RequestParam boolean selected) {
        subscribeCartService.toggleAll(userId, selected);
    }
}
