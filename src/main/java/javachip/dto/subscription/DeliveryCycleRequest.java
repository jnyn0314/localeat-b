package javachip.dto.subscription;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeliveryCycleRequest {
    private String cycleType;   // 예: WEEKLY, MONTHLY
    private int cycleValue;     // 예: 1, 2, 3
}
