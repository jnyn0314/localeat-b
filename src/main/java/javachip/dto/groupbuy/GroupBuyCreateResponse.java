package javachip.dto.groupbuy;

import javachip.entity.GroupBuyStatus;
import javachip.entity.LocalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupBuyCreateResponse {
    private Long groupBuyId;
    private Long productId;
    private String description;
    private LocalDate deadline;
    private int maxParticipants;
    private List<ParticipantResponse> currentParticipants; //  DTO로 교체
    private LocalType local;
    private int partiCount;
    private int payCount;
    private LocalDateTime createdTime;
    private GroupBuyStatus status;
}
