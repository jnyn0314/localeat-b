package javachip.repository;

import javachip.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository // Spring Bean으로 등록함을 명시 (선택적이지만 권장)
public interface UserRepository extends JpaRepository<User, String> { // 엔티티 타입: User, ID 타입: String

    // 사용자 ID로 사용자 찾기 (기본 제공되는 findById와 동일하지만 명시적)
    Optional<User> findByUserId(String userId);

    // 이메일로 사용자 찾기 (로그인 등에 활용 가능, Unique 제약조건이 있다면 Optional)
    Optional<User> findByEmail(String email);

    // 전화번호로 사용자 찾기 (필요에 따라 추가)
    Optional<User> findByPhone(String phone);

    // 이메일 존재 여부 확인
    boolean existsByEmail(String email);

    // 사용자 ID 존재 여부 확인 (기본 제공되는 existsById와 동일하지만 명시적)
    boolean existsByUserId(String userId);

}