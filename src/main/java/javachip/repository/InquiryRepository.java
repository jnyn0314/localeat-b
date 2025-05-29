package javachip.repository;

import javachip.entity.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
    List<Inquiry> findByProductIdOrderByCreatedAtDesc(Long productId);
}