package javachip.entity.groupbuy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantId {
    private String consumer;     // Consumer.userId
    private Integer groupBuy;       // GroupBuy.id
}
