/*
파일명 : ReviewServiceImpl.java
파일설명 : 리뷰 등록 비즈니스 로직 구현체
작성자 : 김민하
작성일 : 2025-05-04
설명 : 주문 항목 유효성 검증, 리뷰 저장, 리뷰 이미지 엔티티 변환 등을 처리함
*/
package javachip.service.impl;

import javachip.dto.review.ReviewDto;
import javachip.entity.*;
import javachip.repository.*;
import javachip.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository     reviewRepository;
    private final OrderItemRepository  orderItemRepository;
    private final ProductRepository    productRepository;
    private final UserRepository       userRepository;

    @Override
    @Transactional
    public ReviewDto createReview(ReviewDto dto) {

        if (reviewRepository.existsByUser_UserIdAndProduct_Id(dto.getUserId(), dto.getProductId())) {
            throw new IllegalStateException("이미 이 상품에 대한 리뷰를 작성하셨습니다.");
        }

        // 주문항목 검증
        OrderItem oi = orderItemRepository.findById(dto.getOrderItemId())
                .orElseThrow(() -> new IllegalArgumentException("OrderItem not found"));
        if (oi.getStatus() != OrderStatus.DELIVERED || oi.isReviewed()) {
            throw new IllegalStateException("Cannot review this item");
        }

        Product p = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        User u = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // DTO → Entity
        Review newReview = dto.toEntity(oi, p, u);
        Review saved     = reviewRepository.save(newReview);

        // 주문항목 리뷰 상태 업데이트
        oi.setReviewed(true);
        orderItemRepository.save(oi);

        return ReviewDto.fromEntity(saved);
    }

    @Override
    @Transactional
    public void deleteReview(Long reviewId) {
        Review r = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));
        // 주문항목 상태 롤백
        OrderItem oi = r.getOrderItem();
        oi.setReviewed(false);
        orderItemRepository.save(oi);
        // cascade/orphanRemoval 로 이미지 자동 삭제
        reviewRepository.delete(r);
    }

    @Override
    public List<Long> getReviewedProductIds(String userId) {
        return reviewRepository.findReviewedProductIdsByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewDto> getReviewsByProduct(Long productId, String sortBy, String currentUserId) {
        List<Review> list = reviewRepository.findByProductId(productId, sortBy);
        List<ReviewDto> dtos = list.stream()
                .map(ReviewDto::fromEntity)
                .collect(Collectors.toList());

        // 비로그인 사용자는 정렬 없이 전체 리뷰 반환
        if (currentUserId == null || currentUserId.isBlank()) {
            return dtos;
        }

        // 로그인 사용자: 본인 리뷰만 맨 위로 올리기 (여러 개면 첫 번째만)
        dtos.stream()
                .filter(d -> currentUserId.equals(d.getUserId()))
                .findFirst()
                .ifPresent(me -> {
                    dtos.remove(me);
                    dtos.add(0, me);
                });

        return dtos;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewDto> getMyReviews(String userId) {
        return reviewRepository.findByUser_UserId(userId).stream()
                .map(ReviewDto::fromEntity)
                .collect(Collectors.toList());
    }
}
