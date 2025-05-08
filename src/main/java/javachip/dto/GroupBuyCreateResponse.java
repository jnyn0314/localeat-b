package javachip.dto;

import javachip.entity.GroupBuyStatus;
import javachip.entity.LocalType;
import javachip.entity.Participant;
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
    private List<Participant> currentParticipants;
    private LocalType local;
    private int partiCount;
    private int payCount;
    private LocalDateTime createdTime;
    private GroupBuyStatus status;
}
