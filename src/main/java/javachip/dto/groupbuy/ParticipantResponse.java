package javachip.dto.groupbuy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParticipantResponse {
    private String consumerId;
    private int quantity;
    private boolean payment;
}