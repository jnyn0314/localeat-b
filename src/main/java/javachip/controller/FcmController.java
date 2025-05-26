package javachip.controller;

import com.google.firebase.messaging.FirebaseMessagingException;
import javachip.dto.alarm.FcmTokenRequest;
import javachip.repository.FcmTokenRepository;
import javachip.service.FcmService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/fcm")
@RequiredArgsConstructor
public class FcmController {

    private final FcmTokenRepository tokenRepository;
    private final FcmService fcmService;

    @PostMapping("/token")
    public ResponseEntity<Void> saveToken(@RequestBody FcmTokenRequest request) {
        fcmService.saveOrUpdateToken(request.getUserId(), request.getToken());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/send")
    public ResponseEntity<Void> sendTestNotification(@RequestBody FcmTokenRequest request) throws FirebaseMessagingException {
        String token = tokenRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("토큰 없음"))
                .getToken();

        fcmService.sendNotificationToUser(token, "테스트 알림", "푸시 알림이 도착했습니다!");
        return ResponseEntity.ok().build();
    }
}

