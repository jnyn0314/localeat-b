package javachip.repository;

import javachip.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByCartIdAndProductId(Long cartId, Long productId);
    List<CartItem> findAllByCartId(Long cartId);
}