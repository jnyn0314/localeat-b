package javachip.service;

import javachip.entity.*;
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
    private final GroupBuyRepository gbRepo;
    private final AlarmService alarmService;

    /** 1분마다 만료된 공구장바구니 아이템 정리 */
    @Scheduled(fixedRate = 60_000)
    public void expireUnpaidItems() {
        LocalDateTime now = LocalDateTime.now();
        List<GroupBuyCartItem> expired = repo.findAllByExpiresAtBefore(now);

        //결제 실패 시
        for (GroupBuyCartItem item : expired) {
            if (item.getPaymentStatus() == PaymentStatus.COMPLETED) continue;

            item.setPaymentStatus(PaymentStatus.EXPIRED);
            repo.save(item);

            GroupBuy gb = item.getGroupBuy();
            gb.setStatus(GroupBuyStatus.FAILED);
            gbRepo.save(gb);

            for (Participant p : gb.getParticipants()) {
                alarmService.notifyGroupBuyFailureToBuyer(p.getConsumer(), gb.getProduct(), "PAYMENT_FAILED");
            }
        }


        // 🔔 조건 ①: 모집 실패 (RECRUITING + 마감 시간 경과)
        List<GroupBuy> expiredRecruiting = gbRepo.findAllByStatusAndTimeBefore(GroupBuyStatus.RECRUITING, now);
        for (GroupBuy gb : expiredRecruiting) {
            gb.setStatus(GroupBuyStatus.FAILED);
            gbRepo.save(gb);

            for (Participant p : gb.getParticipants()) {
                alarmService.notifyGroupBuyFailureToBuyer(p.getConsumer(), gb.getProduct(), "RECRUIT_FAILED");
            }
        }
    }

}

