package javachip.repository;


import com.yourproject.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SellerRepository extends JpaRepository<Seller, String> { // 엔티티 타입: Seller, ID 타입: String

    // Seller ID로 찾기
    Optional<Seller> findByUserId(String userId);

    // 사업자 등록 번호로 Seller 찾기 (Unique하다면 Optional)
    Optional<Seller> findByBusinessId(String businessId);

}