package javachip.dto.groupbuy;

import javachip.entity.LocalType;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupBuyListResponse {
    private Integer groupBuyId;
    private String productName;
    private LocalType local;
    private Integer maxParticipants;
    private Integer partiCount;
}