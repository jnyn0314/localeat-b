package javachip.dto.inquiry;

import javachip.entity.*;
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
