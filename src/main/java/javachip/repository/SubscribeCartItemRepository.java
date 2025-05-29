/*
파일명: SubscribeCartItemRepository.java
설명: 구독 장바구니 항목에 대한 JPA Repository.
*/

package javachip.repository;

import javachip.entity.SubscribeCartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscribeCartItemRepository extends JpaRepository<SubscribeCartItem, Long> {
    List<SubscribeCartItem> findByCart_Consumer_Id(String consumerId);
}