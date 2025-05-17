package javachip.dto.groupbuy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@Builder
public class MyGroupBuyStatusResponse {
    private Long groupBuyId;
    private Long productId;
    private String productName;
    private int currentCount;
    private int maxParticipants;
    private LocalDate deadline;
}
