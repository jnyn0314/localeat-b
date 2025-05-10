/*
파일명 : ReviewController.java
파일설명 : 리뷰 등록 요청을 처리하는 REST 컨트롤러
작성자 : 김민하
작성일 : 2025-05-04
*/
package javachip.controller;

import javachip.dto.ReviewDto;
import javachip.service.FileStorageService;
import javachip.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {
    private final ReviewService reviewService;
    private final FileStorageService fileStorageService;

    // 리뷰 등록 (FormData 바인딩)
    @PostMapping
    public ResponseEntity<ReviewDto> createReview(
            @ModelAttribute ReviewDto reviewDto,
            @RequestParam(value = "images", required = false) List<MultipartFile> images
    ) {
        // 👇 디버깅용 출력 추가
        System.out.println("💬 수신된 ReviewDto: " + reviewDto);
        System.out.println("💬 수신된 이미지 수: " + (images != null ? images.size() : 0));

        if (images != null && !images.isEmpty()) {
            List<String> urls = images.stream()
                    .filter(f -> !f.isEmpty())
                    .map(fileStorageService::store)
                    .collect(Collectors.toList());
            reviewDto.setImageUrls(urls);
        }
        ReviewDto saved = reviewService.createReview(reviewDto);
        return ResponseEntity.ok(saved);
    }

    // 리뷰 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }

    // 상품별 리뷰 조회 (정렬, 본인 리뷰 상단 처리)
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ReviewDto>> getReviewsByProduct(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "latest") String sortBy,
            @RequestParam String currentUserId
    ) {
        List<ReviewDto> reviews = reviewService.getReviewsByProduct(
                productId, sortBy, currentUserId
        );
        return ResponseEntity.ok(reviews);
    }

    // 리뷰 목록 조회
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReviewDto>> getMyReviews(
            @PathVariable String userId
    ) {
        List<ReviewDto> myReviews = reviewService.getMyReviews(userId);
        return ResponseEntity.ok(myReviews);
    }
}

