package javachip.dto.alarm;

import javachip.entity.alarm.Alarm;
import javachip.entity.alarm.NotificationType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AlarmResponseDTO {
    private Long id;
    private String userId;
    private NotificationType type;  // 알림 타입
    private String message;         // 알림 메시지
    private LocalDateTime timestamp; // 알림 시간
    private String isRead;          // 읽음 여부 (Y/N)
    private Long orderId;         // 주문 관련 알림인 경우

    // 엔티티를 DTO로 변환하는 정적 메서드
    public static AlarmResponseDTO from(Alarm alarm) {
        AlarmResponseDTO dto = new AlarmResponseDTO();
        dto.setId(alarm.getId());
        dto.setUserId(alarm.getUser().getUserId());
        dto.setType(alarm.getType());
        dto.setMessage(alarm.getMessage());
        dto.setTimestamp(alarm.getTimestamp());
        dto.setIsRead(alarm.getIsRead());  // String "Y"/"N" 그대로 사용

        // 주문 관련 알림인 경우 orderId 설정
        if (alarm.getOrder() != null) {
            dto.setOrderId(alarm.getOrder().getId());
        }

        return dto;
    }
}