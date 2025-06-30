package javachip.repository.alarm;

import javachip.entity.alarm.FcmToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FcmTokenRepository extends JpaRepository<FcmToken, String> {
    // 수정: FcmToken 엔티티를 반환하도록 변경
    FcmToken findByUserId(String userId);

    // 추가: userId로 토큰 존재 여부 확인
    boolean existsByUserId(String userId);

    // 추가: userId로 토큰 삭제
    void deleteByUserId(String userId);
}
