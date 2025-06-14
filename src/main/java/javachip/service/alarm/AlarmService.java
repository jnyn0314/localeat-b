package javachip.service.alarm;

import jakarta.transaction.Transactional;
import javachip.entity.alarm.Alarm;
import javachip.entity.alarm.NotificationType;
import javachip.entity.alarm.OrderAlarm;
import javachip.entity.cart.Consumer;
import javachip.entity.groupbuy.GroupBuy;
import javachip.entity.inquiry.Inquiry;
import javachip.entity.order.OrderItem;
import javachip.entity.order.OrderStatus;
import javachip.entity.product.Product;
import javachip.entity.user.Seller;
import javachip.repository.alarm.AlarmRepository;
import javachip.repository.alarm.OrderAlarmRepository;
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
        Product product = orderItem.getProduct();
        if (product == null) {
            return;
        }
        Seller seller = product.getSeller();
        if (seller == null) {
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
        } catch (Exception e) {
            throw new RuntimeException("알림 처리 실패", e);
        }
    }

    //공동구매 성공 알림(판매자)
    public void notifySellerOnGroupBuyOrderSuccess(OrderItem orderItem, GroupBuy groupBuy) {
        Product product = orderItem.getProduct();
        Seller seller = product.getSeller();

        if (seller == null) {
            return;
        }
        String message = String.format("[공동 구매 주문] '%s' 상품이 주문되었습니다.",
                product.getProductName());

        try {
            Alarm alarm = Alarm.builder()
                    .type(NotificationType.GROUP_BUY)
                    .message(message)
                    .timestamp(LocalDateTime.now())
                    .user(seller)
                    .order(orderItem.getOrder())
                    .isRead("N")
                    .build();

            alarmRepository.save(alarm);

            OrderAlarm orderAlarm = OrderAlarm.builder()
                    .alarm(alarm)
                    .order(orderItem.getOrder())
                    .product(product)
                    .build();

            orderAlarmRepository.save(orderAlarm);

            fcmService.sendNotificationToUser(
                    seller.getUserId(),
                    "공동구매 주문 성공 알림",
                    message
            );

        } catch (Exception e) {
            throw new RuntimeException("공동구매 알림 실패", e);
        }
    }

    //공동구매 성공알림(구매자에게)
    public void notifyGroupBuySuccessToBuyer(Consumer consumer, GroupBuy groupBuy) {
        String message = String.format("[공동구매 주문] '%s' 상품 공동구매가 성공적으로 완료되었습니다.",
                groupBuy.getProduct().getProductName());

        try {
            Alarm alarm = Alarm.builder()
                    .type(NotificationType.GROUP_BUY)
                    .message(message)
                    .timestamp(LocalDateTime.now())
                    .user(consumer)
                    .isRead("N")
                    .build();

            alarmRepository.save(alarm);

            fcmService.sendNotificationToUser(
                    consumer.getUserId(),
                    "공동구매 주문 완료",
                    message
            );

        } catch (Exception e) {
            throw new RuntimeException("공동구매 성공 알림 실패", e);
        }
    }

    //공동구매 실패 알람(구매자에게 보냄)
    public void notifyGroupBuyFailureToBuyer(Consumer consumer, Product product, String reason) {
        String message;

        if ("RECRUIT_FAILED".equals(reason)) {
            message = String.format("[공동구매 실패] '%s' 상품의 공동구매가 인원 부족으로 종료되었습니다.",
                    product.getProductName());
        } else if ("PAYMENT_FAILED".equals(reason)) {
            message = String.format("[공동구매 실패] '%s' 상품의 결제가 마감 기한 내에 완료되지 않았습니다.",
                    product.getProductName());
        } else {
            message = String.format("[공동구매 실패] '%s' 상품의 공동구매가 실패하였습니다.",
                    product.getProductName());
        }

        try {
            Alarm alarm = Alarm.builder()
                    .type(NotificationType.GROUP_BUY)
                    .message(message)
                    .timestamp(LocalDateTime.now())
                    .user(consumer)
                    .isRead("N")
                    .build();

            alarmRepository.save(alarm);
            fcmService.sendNotificationToUser(consumer.getUserId(), "공동구매 실패", message);

        } catch (Exception e) {
            System.out.println("공동구매 실패 알림 전송 실패: " + e.getMessage());
        }
    }



    private NotificationType getTypeFromOrderItem(OrderItem item) {
        if (item.isSubscription()) return NotificationType.SUBSCRIPTION;
        if (item.isGroupBuy()) return NotificationType.GROUP_BUY;
        return NotificationType.ORDER;
    }

    private String generateMessage(OrderItem item) {
        String productName = item.getProduct().getProductName();

        if (item.isSubscription()) {
            return String.format("[구독 주문] '%s' 상품이 주문되었습니다.", productName);
        } else {
            return String.format("[일반 주문] '%s' 상품이 주문되었습니다.", productName);
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
        System.out.println("✅ 알림 읽음 처리 완료: alarmId=" + alarmId);
    }

    @Transactional
    public void deleteAlarm(Long alarmId) {
        try {
            // OrderAlarm 먼저 삭제
            orderAlarmRepository.deleteByAlarmId(alarmId);

            // 그 다음 Alarm 삭제
            Alarm alarm = alarmRepository.findById(alarmId)
                    .orElseThrow(() -> new RuntimeException("알림을 찾을 수 없습니다."));
            alarmRepository.delete(alarm);

            System.out.println("✅ 알림 삭제 완료: alarmId=" + alarmId);
        } catch (Exception e) {
            System.out.println("❌ 알림 삭제 실패: " + e.getMessage());
            throw new RuntimeException("알림 삭제 실패", e);
        }
    }

    //배송상태변경
    public void notifyBuyerOnOrderStatusChange(OrderItem orderItem) {
        String buyerId = orderItem.getUserId();

        if (buyerId == null || buyerId.isBlank()) {
            System.out.println("❌ 구매자 ID 없음");
            return;
        }

        NotificationType type = NotificationType.DELIVERY;
        String message = generateStatusMessage(orderItem);

        try {
            Consumer consumer = new Consumer();
            consumer.setUserId(buyerId);

            Alarm alarm = Alarm.builder()
                    .type(type)
                    .message(message)
                    .timestamp(LocalDateTime.now())
                    .user(consumer)  // ✅ Consumer 객체 사용
                    .order(orderItem.getOrder())
                    .isRead("N")
                    .build();


            alarmRepository.save(alarm);

            fcmService.sendNotificationToUser(
                    buyerId,
                    "배송 상태 변경 알림",
                    message
            );

            System.out.println("✅ 구매자 배송 상태 알림 전송 완료 - " + message);

        } catch (Exception e) {
            System.out.println("❌ 알림 처리 실패: " + e.getMessage());
            throw new RuntimeException("배송 상태 알림 실패", e);
        }
    }

    private String generateStatusMessage(OrderItem item) {
        String productName = item.getProduct().getProductName();
        OrderStatus status = item.getStatus();

        return switch (status) {
            case READY -> "[배송 준비 중] " + productName + "이(가) 곧 배송됩니다.";
            case DELIVERING -> "[배송 중] " + productName + "이(가) 배송 중입니다.";
            case DELIVERED -> "[배송 완료] " + productName + "이(가) 도착했습니다.";
            default -> "[알림] " + productName + "의 배송 상태가 변경되었습니다.";
        };
    }

    @Transactional
    public void notifyGroupBuyCompletion(Consumer consumer, GroupBuy groupBuy) {
        String message = String.format("[공동구매 모집 완료] 장바구니에서 결제를 진행해주세요.",
                groupBuy.getProduct().getProductName());

        try {
            Alarm alarm = Alarm.builder()
                    .type(NotificationType.GROUP_BUY)
                    .message(message)
                    .timestamp(LocalDateTime.now())
                    .user(consumer)
                    .build();
            alarm.setIsRead("N");

            alarmRepository.save(alarm);

            // FCM 푸시 알림 전송
            fcmService.sendNotificationToUser(
                    consumer.getUserId(),
                    "공동구매 모집 완료",
                    message
            );

            System.out.println("✅ 공동구매 완료 알림 생성 및 전송 완료 - " + alarm.getMessage());

        } catch (Exception e) {
            System.out.println("❌ 공동구매 완료 알림 저장 실패: " + e.getMessage());
            throw new RuntimeException("공동구매 완료 알림 처리 실패", e);
        }
    }

    /**
     * 문의 등록 시 판매자에게 알림을 전송합니다.
     */
    public void notifySellerOnInquiry(Inquiry inquiry) {
        Product product = inquiry.getProduct();
        Seller seller = product.getSeller();

        if (seller == null) {
            System.out.println("❌ 판매자 정보 없음. 알림 생략.");
            return;
        }

        String message = String.format("[문의 등록] '%s' 상품에 새로운 문의가 등록되었습니다.",
                product.getProductName());

        try {
            Alarm alarm = Alarm.builder()
                    .type(NotificationType.INQUIRY)
                    .message(message)
                    .timestamp(LocalDateTime.now())
                    .user(seller)
                    .isRead("N")
                    .build();

            alarmRepository.save(alarm);

            fcmService.sendNotificationToUser(
                    seller.getUserId(),
                    "새로운 문의 알림",
                    message
            );

            System.out.println("✅ 문의 등록 알림 전송 완료 - " + message);

        } catch (Exception e) {
            System.out.println("❌ 문의 등록 알림 실패: " + e.getMessage());
            throw new RuntimeException("문의 등록 알림 실패", e);
        }
    }

    /**
     * 문의 답변 등록 시 구매자에게 알림을 전송합니다.
     */
    public void notifyBuyerOnInquiryAnswer(Inquiry inquiry) {
        Consumer consumer = inquiry.getConsumer();
        Product product = inquiry.getProduct();

        if (consumer == null) {
            System.out.println("❌ 구매자 정보 없음. 알림 생략.");
            return;
        }

        String message = String.format("[문의 답변] '%s' 상품의 문의에 답변이 등록되었습니다.",
                product.getProductName());

        try {
            Alarm alarm = Alarm.builder()
                    .type(NotificationType.INQUIRY)
                    .message(message)
                    .timestamp(LocalDateTime.now())
                    .user(consumer)
                    .isRead("N")
                    .build();

            alarmRepository.save(alarm);

            fcmService.sendNotificationToUser(
                    consumer.getUserId(),
                    "문의 답변 알림",
                    message
            );

            System.out.println("✅ 문의 답변 알림 전송 완료 - " + message);

        } catch (Exception e) {
            System.out.println("❌ 문의 답변 알림 실패: " + e.getMessage());
            throw new RuntimeException("문의 답변 알림 실패", e);
        }
    }
}
