package javachip.repository;

import javachip.entity.GroupBuyCartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface GroupBuyCartItemRepository extends JpaRepository<GroupBuyCartItem,Long> {
    List<GroupBuyCartItem> findAllByExpiresAtBefore(LocalDateTime now);
    List<GroupBuyCartItem> findByCartItem_Cart_Consumer_UserId(String userId);
}
