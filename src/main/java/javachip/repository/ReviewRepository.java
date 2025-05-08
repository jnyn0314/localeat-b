package javachip.repository;

import javachip.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("SELECT r FROM Review r WHERE r.product.id = :productId " +
            "ORDER BY CASE WHEN :sortBy = 'highest' THEN r.rating END DESC, " +
            "CASE WHEN :sortBy = 'latest' THEN r.createdAt END DESC")
    List<Review> findByProductId(Long productId, String sortBy);

    // User.userId 가 String 이므로
    List<Review> findByUser_UserId(String userId);
}
