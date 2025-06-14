package javachip.dto.subscription;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionResponseDto {
    private Long id; // ← Subscription의 식별자
    private String productName;
    private String deliveryCycleType;
    private int deliveryCycleValue;
    private int quantity;
    private String startDate;
}