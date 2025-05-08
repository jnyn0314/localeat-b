package javachip.repository;

import javachip.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findById(String userId); // 정여진 수정햇어여(05/07)

    boolean existsByUserId(String userId);

}