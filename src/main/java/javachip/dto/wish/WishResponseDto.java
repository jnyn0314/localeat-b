/*
    파일명 : WishResponseDto.java
    설명 : Wish 엔티티를 프론트로 전달할 때 사용하는 응답 DTO 클래스입니다.
    작성자 : 정여진
    작성일 : 2025.05.18.
*/
package javachip.dto.wish;

import javachip.entity.product.Product;
import javachip.entity.wish.Wish;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WishResponseDto {
    private Long wishId;
    private Long productId;
    private String productName;

    public static WishResponseDto from(Wish wish) {
        Product p = wish.getProduct();
        return WishResponseDto.builder()
                .wishId(wish.getId())
                .productId(p.getId())
                .productName(p.getProductName())
                .build();
    }
}