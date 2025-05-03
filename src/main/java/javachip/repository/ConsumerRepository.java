package javachip.repository;

import com.yourproject.entity.Consumer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConsumerRepository extends JpaRepository<Consumer, String> { // 엔티티 타입: Consumer, ID 타입: String

    // Consumer ID로 찾기 (UserRepository의 findById와 유사하지만 Consumer 타입 반환)
    Optional<Consumer> findByUserId(String userId);

    // 생년월일로 Consumer 찾기 (결과가 여러 개일 수 있음)
    List<Consumer> findByBirth(String birth);

    // 특정 Cart ID를 가진 Consumer 찾기 (Cart 엔티티 및 관계 설정 필요)
    // Optional<Consumer> findByCart_CartId(Long cartId); // Cart 엔티티의 ID 필드명 확인 필요
}