package javachip.repository.groupbuy;

import javachip.entity.groupbuy.GroupBuy;
import javachip.entity.groupbuy.GroupBuyStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface GroupBuyRepository extends JpaRepository<GroupBuy, Long> {
    List<GroupBuy> findByProduct_IdAndStatusAndTimeAfter(
            Long productId,
            GroupBuyStatus status,
            LocalDateTime now
    );
    List<GroupBuy> findAllByStatusAndTimeBefore(GroupBuyStatus status, LocalDateTime time);

}
