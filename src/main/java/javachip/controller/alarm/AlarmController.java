package javachip.controller.alarm;

import javachip.dto.alarm.AlarmResponseDTO;
import javachip.entity.alarm.Alarm;
import javachip.service.alarm.AlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/alarms")
public class AlarmController {

    private final AlarmService alarmService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<AlarmResponseDTO>> getUserAlarms(@PathVariable String userId) {
        try {
            List<Alarm> alarms = alarmService.getUserAlarms(userId);
            List<AlarmResponseDTO> alarmDtos = alarms.stream()
                    .map(AlarmResponseDTO::from)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(alarmDtos);
        } catch (Exception e) {
            System.out.println("❌ 알림 조회 실패: " + e.getMessage());
            throw new RuntimeException("알림 조회 실패", e);
        }
    }

    // 알림 읽음 처리
    @PostMapping("/read/{alarmId}")
    public ResponseEntity<Void> markAlarmAsRead(@PathVariable Long alarmId) {
        try {
            alarmService.markAlarmAsRead(alarmId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.out.println("❌ 알림 읽음 처리 실패: " + e.getMessage());
            throw new RuntimeException("알림 읽음 처리 실패", e);
        }
    }

    // 알림 삭제
    @DeleteMapping("/{alarmId}")
    public ResponseEntity<Void> deleteAlarm(@PathVariable Long alarmId) {
        try {
            alarmService.deleteAlarm(alarmId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.out.println("❌ 알림 삭제 실패: " + e.getMessage());
            throw new RuntimeException("알림 삭제 실패", e);
        }
    }
}

