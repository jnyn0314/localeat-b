package javachip.repository;

import javachip.entity.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    List<Alarm> findByUser_UserIdOrderByTimestampDesc(String userId);
}

