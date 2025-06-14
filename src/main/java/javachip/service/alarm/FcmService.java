package javachip.service.alarm;

import com.google.firebase.messaging.*;
import javachip.entity.alarm.FcmToken;
import javachip.repository.alarm.FcmTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FcmService {
    private final FcmTokenRepository tokenRepository;

    @Transactional
    public void saveOrUpdateToken(String userId, String token) {
        if (token == null || token.isBlank()) {
            System.out.println("❌ 유효하지 않은 토큰입니다. userId=" + userId);
            return;
        }

        try {
            // 기존 토큰이 있으면 삭제
            if (tokenRepository.existsByUserId(userId)) {
                System.out.println("기존 토큰 삭제: userId=" + userId);
                tokenRepository.deleteByUserId(userId);
            }

            // 새 토큰 저장
            FcmToken fcmToken = new FcmToken();
            fcmToken.setUserId(userId);
            fcmToken.setToken(token);
            tokenRepository.save(fcmToken);

            System.out.println("✅ FCM 토큰 저장/업데이트 성공: userId=" + userId);
        } catch (Exception e) {
            System.out.println("❌ FCM 토큰 저장 실패: " + e.getMessage());
            throw new RuntimeException("FCM 토큰 저장 실패", e);
        }
    }

    @Transactional(readOnly = true)
    public void sendNotificationToUser(String userId, String title, String message) {
        // 수정: FcmToken 엔티티에서 토큰 값을 가져옴
        FcmToken fcmToken = tokenRepository.findByUserId(userId);
        if (fcmToken == null || fcmToken.getToken() == null || fcmToken.getToken().isBlank()) {
            System.out.println("❌ FCM 토큰이 없습니다. userId=" + userId);
            return;
        }

        Message fcmMessage = Message.builder()
                .setToken(fcmToken.getToken())  // 수정: 엔티티에서 토큰 값 사용
                .setNotification(Notification.builder()
                        .setTitle(title)
                        .setBody(message)
                        .build())
                .build();

        try {
            String response = FirebaseMessaging.getInstance().send(fcmMessage);
            System.out.println("✅ FCM 푸시 성공: " + response);
        } catch (FirebaseMessagingException e) {
            // 토큰이 유효하지 않은 경우 삭제
            if (e.getMessagingErrorCode() == MessagingErrorCode.INVALID_ARGUMENT ||
                    e.getMessagingErrorCode() == MessagingErrorCode.UNREGISTERED) {
                System.out.println("❌ 유효하지 않은 토큰 삭제: userId=" + userId);
                tokenRepository.deleteByUserId(userId);
            }
            System.out.println("❌ FCM 푸시 실패: " + e.getMessage());
            throw new RuntimeException("FCM 푸시 알림 전송 실패", e);
        }
    }
}