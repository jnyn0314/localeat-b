package javachip.service;

import javachip.entity.GroupBuy;
import javachip.entity.GroupBuyCartItem;
import javachip.entity.GroupBuyStatus;
import javachip.entity.PaymentStatus;
import javachip.repository.GroupBuyCartItemRepository;
import javachip.repository.GroupBuyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GroupBuyCartScheduler {

    private final GroupBuyCartItemRepository repo;
    private final GroupBuyRepository      gbRepo;

    /** 1분마다 만료된 공구장바구니 아이템 정리 */
    @Scheduled(fixedRate = 60_000)
    public void expireUnpaidItems() {
        LocalDateTime now = LocalDateTime.now();
        // 24시간 지난 모든 항목 조회
        List<GroupBuyCartItem> expired = repo.findAllByExpiresAtBefore(now);

        for (GroupBuyCartItem item : expired) {
            // 1) 결제 완료된 항목은 건너뛴다
            if (item.getPaymentStatus() == PaymentStatus.COMPLETED) {
                continue;
            }

            // 2) 아직 결제되지 않은(PENDING) 항목 → 만료 처리
            // 옵션: 상태를 EXPIRED로 업데이트한 뒤 삭제할 수도 있습니다.
            item.setPaymentStatus(PaymentStatus.EXPIRED);
            repo.save(item);

            // 3) 장바구니 항목 삭제
            repo.delete(item);

            // 4) 공동구매 전체를 실패 상태로 전환
            GroupBuy gb = item.getGroupBuy();
            gb.setStatus(GroupBuyStatus.FAILED);
            gbRepo.save(gb);

            // 5) (선택) 사용자 알림 서비스 호출
        }
    }
}

