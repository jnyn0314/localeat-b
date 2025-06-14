package javachip.dto.groupbuy;

import javachip.entity.groupbuy.GroupBuyStatus;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDate;
import java.util.List;

@Getter @Builder
public class GroupBuyDetailResponse {
    private Long groupBuyId;
    private Long productId;
    private String productName;
    private String description;
    private LocalDate deadline;
    private String remainingTime;    // HH:mm:ss 포맷
    private int partiCount;
    private int maxParticipants;
    private GroupBuyStatus status;
    private List<ParticipantDetailResponse> participants;
}
