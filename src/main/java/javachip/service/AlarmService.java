package javachip.service;

import jakarta.transaction.Transactional;
import javachip.entity.*;
import javachip.repository.AlarmRepository;
import javachip.repository.OrderAlarmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlarmService {

    private final AlarmRepository alarmRepository;
    private final OrderAlarmRepository orderAlarmRepository; // 추가
    private final FcmService fcmService;

    public void notifySellerOnOrder(OrderItem orderItem) {
        System.out.println("💡 알림 생성 시작 for OrderItem ID: " + orderItem.getId());
        Product product = orderItem.getProduct();
        if (product == null) {
            System.out.println("❌ 상품이 없습니다.");
            return;
        }
        Seller seller = product.getSeller();
        if (seller == null) {
            System.out.println("❌ 판매자가 없습니다.");
            return;
        }

        NotificationType type = getTypeFromOrderItem(orderItem);
        String message = generateMessage(orderItem);

        try {
            Alarm alarm = Alarm.builder()
                    .type(type)
                    .message(message)
                    .timestamp(LocalDateTime.now())
                    .user(seller)
                    .order(orderItem.getOrder())
                    .build();
            alarm.setIsRead("N");

            alarmRepository.save(alarm);

            OrderAlarm orderAlarm = OrderAlarm.builder()
                    .alarm(alarm)
                    .order(orderItem.getOrder())
                    .product(orderItem.getProduct())
                    .build();
            orderAlarmRepository.save(orderAlarm);

            // 2. FCM 푸시 알림 전송
            fcmService.sendNotificationToUser(
                    seller.getUserId(),
                    "새로운 주문 알림",
                    message
            );

            System.out.println("✅ 알림 생성, DB 저장, FCM 전송 완료 - " + alarm.getMessage());


        } catch (Exception e) {
            System.out.println("❌ 알림 저장 실패: " + e.getMessage());
            throw new RuntimeException("알림 처리 실패", e);
        }
    }

    private NotificationType getTypeFromOrderItem(OrderItem item) {
        if (item.isSubscription()) return NotificationType.SUBSCRIPTION;
        if (item.isGroupBuy()) return NotificationType.GROUP_BUY;
        return NotificationType.ORDER;
    }

    private String generateMessage(OrderItem item) {
        if (item.isSubscription()) {
            return "[구독 주문] 상품이 주문되었습니다.";
        } else if (item.isGroupBuy()) {
            return "[공동구매] 상품이 주문되었습니다.";
        } else {
            return "[일반 주문] 상품이 주문되었습니다.";
        }
    }

    public List<Alarm> getUserAlarms(String userId) {
        // 메서드명 변경
        return alarmRepository.findByUserUserIdOrderByTimestampDesc(userId);
    }

    @Transactional
    public void markAlarmAsRead(Long alarmId) {
        Alarm alarm = alarmRepository.findById(alarmId)
                .orElseThrow(() -> new RuntimeException("알림을 찾을 수 없습니다."));
        alarm.setIsRead("Y");
        alarmRepository.save(alarm);
    }
}
