package javachip.repository.review;

import javachip.entity.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("SELECT r FROM Review r WHERE r.product.id = :productId " +
            "ORDER BY CASE WHEN :sortBy = 'highest' THEN r.rating END DESC, " +
            "CASE WHEN :sortBy = 'latest' THEN r.createdAt END DESC")
    List<Review> findByProductId(Long productId, String sortBy);

    boolean existsByUser_UserIdAndProduct_Id(String userId, Long productId);

    @Query("SELECT DISTINCT r.product.id FROM Review r WHERE r.user.userId = :userId")
    List<Long> findReviewedProductIdsByUserId(@Param("userId") String userId);

    List<Review> findByUser_UserId(String userId);
}
