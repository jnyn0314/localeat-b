/*
파일명 : WishController.java
파일설명 : 찜(Wish) 기능의 API 엔드포인트를 제공하는 컨트롤러 클래스입니다.
작성자 : 정여진
작성일 : 2025-05-17.
*/
package javachip.controller;

import javachip.dto.ProductDto;
import javachip.service.WishService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/wish")
@RequiredArgsConstructor
public class WishController {

    private final WishService wishService;

    // 찜 토글
    @PostMapping("/{productId}")
    public ResponseEntity<?> toggleWish(@PathVariable Long productId, @RequestParam String userId) {
        boolean isWished = wishService.toggleWish(userId, productId);
        return ResponseEntity.ok(Map.of("wished", isWished));
    }

    // 사용자별 찜한 상품 목록 조회
    @GetMapping
    public ResponseEntity<List<ProductDto>> getWishedList(@RequestParam String userId) {
        return ResponseEntity.ok(wishService.getWishedProducts(userId));
    }
}
