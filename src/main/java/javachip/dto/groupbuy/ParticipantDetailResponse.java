package javachip.dto.groupbuy;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ParticipantDetailResponse {
    private String consumerId;
    private int quantity;
}
