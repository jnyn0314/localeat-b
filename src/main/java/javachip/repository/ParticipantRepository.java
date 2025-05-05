package javachip.repository;

import javachip.entity.Participant;
import javachip.entity.ParticipantId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantRepository extends JpaRepository<Participant, ParticipantId> {
}
