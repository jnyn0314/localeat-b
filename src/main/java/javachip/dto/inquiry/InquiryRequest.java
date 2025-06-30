package javachip.dto.inquiry;

import javachip.entity.cart.Consumer;
import javachip.entity.inquiry.Inquiry;
import javachip.entity.inquiry.InquiryCategory;
import javachip.entity.product.Product;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InquiryRequest {

    private Long productId;
    private String userId;
    private InquiryCategory category;
    private String content;

    public Inquiry toEntity(Product product, Consumer consumer) {
        return Inquiry.builder()
                .product(product)
                .consumer(consumer)
                .category(category)
                .content(content)
                .build();
    }
}
