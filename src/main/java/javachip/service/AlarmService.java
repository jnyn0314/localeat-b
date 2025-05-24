package javachip.service;

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

        Alarm alarm = Alarm.builder()
                .type(type)
                .message(message)
                .timestamp(LocalDateTime.now())
                .user(seller)
                .order(orderItem.getOrder())
                .build();
        alarm.setIsRead("N");

        try {
            alarmRepository.save(alarm);

            OrderAlarm orderAlarm = OrderAlarm.builder()
                    .alarm(alarm)
                    .order(orderItem.getOrder())
                    .product(orderItem.getProduct())
                    .build();
            orderAlarmRepository.save(orderAlarm);

            System.out.println("✅ 알림 생성 및 연결 완료 - " + alarm.getMessage());

        } catch (Exception e) {
            System.out.println("❌ 알림 저장 실패: " + e.getMessage());
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

    public void markAsRead(Long alarmId) {
        Alarm alarm = alarmRepository.findById(alarmId)
                .orElseThrow(() -> new RuntimeException("알림을 찾을 수 없습니다."));
        alarm.setIsRead("N");
    }

    public List<Alarm> getUserAlarms(String userId) {
        return alarmRepository.findByUser_UserIdOrderByTimestampDesc(userId);
    }
}
