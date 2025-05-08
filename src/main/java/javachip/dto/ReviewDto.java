package javachip.dto;

import javachip.entity.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class ReviewDto {
    private Long id;
    private Long orderItemId;
    private Long productId;
    private String userId;          // User.userId (String)
    private int rating;
    private String content;
    private LocalDateTime createdAt;
    private List<String> imageUrls; // ReviewImage.imageUrl 리스트

    /** Entity → DTO */
    public static ReviewDto fromEntity(Review r) {
        return ReviewDto.builder()
                .id(r.getId())
                .orderItemId(r.getOrderItem().getId())
                .productId   (r.getProduct().getId())
                .userId      (r.getUser().getUserId())
                .rating      (r.getRating())
                .content     (r.getContent())
                .createdAt   (r.getCreatedAt())
                .imageUrls(r.getImageList().stream()
                        .map(ReviewImage::getImageUrl)
                        .collect(Collectors.toList()))
                .build();
    }

    /** DTO → Entity (연관관계 객체는 인자로 받음) */
    public Review toEntity(OrderItem orderItem, Product product, User user) {
        Review r = Review.builder()
                .orderItem(orderItem)
                .product(product)
                .user(user)
                .rating(rating)
                .content(content)
                .createdAt(createdAt != null ? createdAt : LocalDateTime.now())
                .build();

        // cascade=ALL, orphanRemoval=true 가 잡힌 imageList 필드에 세팅
        if (imageUrls != null) {
            List<ReviewImage> imgs = imageUrls.stream()
                    .map(url -> ReviewImage.builder()
                            .review(r)
                            .imageUrl(url)
                            .build())
                    .collect(Collectors.toList());
            r.setImageList(imgs);
        }
        return r;
    }
}
