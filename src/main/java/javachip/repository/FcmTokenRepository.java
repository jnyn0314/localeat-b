package javachip.repository;

import javachip.entity.FcmToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FcmTokenRepository extends JpaRepository<FcmToken, String> {
    String findTokenByUserId(String userId);
}
