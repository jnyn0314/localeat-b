package javachip.repository;

import javachip.entity.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    List<Alarm> findByUser_UserIdOrderByTimestampDesc(String userId);

    // User 엔티티의 userId로 조회하도록 수정
    // createdAt -> timestamp로 수정
    List<Alarm> findByUserUserIdOrderByTimestampDesc(String userId);
}

