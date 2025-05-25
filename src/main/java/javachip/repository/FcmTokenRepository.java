package javachip.repository;

import javachip.entity.FcmToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FcmTokenRepository extends JpaRepository<FcmToken, String> {
    // 또는 토큰 문자열만 반환하고 싶다면:
    @Query("SELECT t.token FROM FcmToken t WHERE t.userId = :userId")
    String findTokenByUserId(String userId);

    // 수정: FcmToken 엔티티를 반환하도록 변경
    FcmToken findByUserId(String userId);

    // 추가: userId로 토큰 존재 여부 확인
    boolean existsByUserId(String userId);

    // 추가: userId로 토큰 삭제
    void deleteByUserId(String userId);
}
