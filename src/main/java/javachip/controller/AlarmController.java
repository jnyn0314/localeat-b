package javachip.controller;

import javachip.entity.Alarm;
import javachip.service.AlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/alarms")
public class AlarmController {

    private final AlarmService alarmService;

    @GetMapping("/{userId}")
    public List<Alarm> getUserAlarms(@PathVariable String userId) {
        return alarmService.getUserAlarms(userId);
    }

    @PostMapping("/read/{alarmId}")
    public ResponseEntity<Void> markAsRead(@PathVariable Long alarmId) {
        alarmService.markAsRead(alarmId);
        return ResponseEntity.ok().build();
    }
}

