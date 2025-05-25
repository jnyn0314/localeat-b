package javachip.service;

import javachip.repository.FcmTokenRepository;
import lombok.RequiredArgsConstructor;
import oracle.jdbc.driver.Message;
import org.springframework.stereotype.Service;

import javax.management.Notification;

@Service
@RequiredArgsConstructor
public class FcmService {

    private final FcmTokenRepository tokenRepository;

    public void sendNotificationToUser(String userId, String title, String message) {
        String token = tokenRepository.findTokenByUserId(userId);
        if (token == null || token.isBlank()) {
            System.out.println("❌ FCM 토큰이 없습니다. userId=" + userId);
            return;
        }

        Message fcmMessage = Message.builder()
                .setToken(token)
                .setNotification(Notification.builder()
                        .setTitle(title)
                        .setBody(message)
                        .build())
                .build();

        try {
            String response = FirebaseMessaging.getInstance().send(fcmMessage);
            System.out.println("✅ FCM 푸시 성공: " + response);
        } catch (FirebaseMessagingException e) {
            System.out.println("❌ FCM 푸시 실패: " + e.getMessage());
        }
    }
}
