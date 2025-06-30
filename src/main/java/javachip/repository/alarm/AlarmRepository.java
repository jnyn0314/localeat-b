package javachip.repository.alarm;

import javachip.entity.alarm.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    // User 엔티티의 userId로 조회하도록 수정
    // createdAt -> timestamp로 수정
    List<Alarm> findByUserUserIdOrderByTimestampDesc(String userId);
}

