package javachip.repository;

import javachip.entity.OrderAlarm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderAlarmRepository extends JpaRepository<OrderAlarm, Long> {
    void deleteByAlarmId(Long alarmId);
}