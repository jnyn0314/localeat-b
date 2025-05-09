package javachip.repository;

import javachip.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @EntityGraph(attributePaths = "productImages")
    Optional<Product> findWithImagesById(Long id);

    @Query("SELECT p FROM Product p ORDER BY p.createdAt DESC")
    List<Product> findTop8ByOrderByCreatedAtDesc(Pageable pageable);
}