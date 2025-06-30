package javachip.dto.inquiry;

import javachip.entity.inquiry.Inquiry;
import javachip.entity.inquiry.InquiryCategory;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InquiryResponse {

    private Long id;
    private String userId;
    private InquiryCategory category;
    private String content;
    private LocalDateTime createdAt;

    private String answer;
    private LocalDateTime answeredAt;

    public static InquiryResponse fromEntity(Inquiry inquiry) {
        return InquiryResponse.builder()
                .id(inquiry.getId())
                .userId(inquiry.getConsumer().getUserId())
                .category(inquiry.getCategory())
                .content(inquiry.getContent())
                .createdAt(inquiry.getCreatedAt())
                .answer(inquiry.getAnswer())
                .answeredAt(inquiry.getAnsweredAt())
                .build();
    }
}
