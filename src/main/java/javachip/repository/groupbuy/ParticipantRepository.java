package javachip.repository.groupbuy;

import javachip.entity.cart.Consumer;
import javachip.entity.groupbuy.GroupBuy;
import javachip.entity.groupbuy.Participant;
import javachip.entity.groupbuy.ParticipantId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParticipantRepository extends JpaRepository<Participant, ParticipantId> {
    Optional<Participant> findByConsumerAndGroupBuy(Consumer consumer, GroupBuy groupBuy);
    List<Participant> findAllByConsumerUserId(String userId);
}
