package javachip.dto.groupbuy;

import javachip.entity.product.LocalType;
import lombok.*;

import java.time.LocalDate;

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
    private String description;
    private LocalDate deadline;
}