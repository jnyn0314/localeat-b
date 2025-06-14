package javachip.service.cart;

import javachip.entity.cart.GroupBuyCartItem;
import javachip.entity.groupbuy.GroupBuy;
import javachip.entity.groupbuy.GroupBuyStatus;
import javachip.entity.groupbuy.Participant;
import javachip.entity.product.PaymentStatus;
import javachip.repository.groupbuy.GroupBuyCartItemRepository;
import javachip.repository.groupbuy.GroupBuyRepository;
import javachip.service.alarm.AlarmService;
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

