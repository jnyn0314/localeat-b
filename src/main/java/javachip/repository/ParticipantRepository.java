package javachip.repository;

import javachip.entity.Consumer;
import javachip.entity.GroupBuy;
import javachip.entity.Participant;
import javachip.entity.ParticipantId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParticipantRepository extends JpaRepository<Participant, ParticipantId> {
    Optional<Participant> findByConsumerAndGroupBuy(Consumer consumer, GroupBuy groupBuy);

}
