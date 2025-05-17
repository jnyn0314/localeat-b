package javachip.dto.review;

import javachip.entity.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.Collections;
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

    private String productName;

    public static ReviewDto fromEntity(Review r) {
        return ReviewDto.builder()
                .id(r.getId())
                .orderItemId(r.getOrderItem().getId())
                .productId   (r.getProduct().getId())
                .userId      (r.getUser().getUserId())
                .rating      (r.getRating())
                .content     (r.getContent())
                .createdAt   (r.getCreatedAt())
                .imageUrls(r.getImageList() != null
                        ? r.getImageList().stream()
                        .map(ReviewImage::getImageUrl)
                        .collect(Collectors.toList())
                        : Collections.emptyList()
                )

                .productName(r.getProduct().getProductName())
                .build();
    }

    public Review toEntity(OrderItem orderItem, Product product, User user) {
        Review r = Review.builder()
                .orderItem(orderItem)
                .product(product)
                .user(user)
                .rating(rating)
                .content(content)
                .createdAt(createdAt != null ? createdAt : LocalDateTime.now())
                .build();

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
