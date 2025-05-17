/*
파일명 : ReviewService.java
파일설명 : 리뷰 서비스 인터페이스
작성자 : 김민하
작성일 : 2025-05-03
*/
package javachip.service;

import javachip.dto.review.ReviewDto;
import java.util.List;

public interface ReviewService {
    ReviewDto createReview(ReviewDto dto);
    void deleteReview(Long reviewId);
    List<Long> getReviewedProductIds(String userId);
    List<ReviewDto> getReviewsByProduct(Long productId, String sortBy, String currentUserId);
    List<ReviewDto> getMyReviews(String userId);
}
