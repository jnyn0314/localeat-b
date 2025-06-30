package javachip.dto.groupbuy;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GroupBuyParticipationRequest {
    private Long groupBuyId;
    private int quantity;
}
