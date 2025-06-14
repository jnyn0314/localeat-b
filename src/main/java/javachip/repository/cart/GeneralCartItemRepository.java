package javachip.repository.cart;

import javachip.entity.cart.GeneralCartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GeneralCartItemRepository extends JpaRepository<GeneralCartItem, Long> {
}
