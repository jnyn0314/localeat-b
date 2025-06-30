/*
파일명 : OrderItemRepository.java
파일설명 : OrderItem 레포지토리
작성자 : 정여진
기간 : 2025-05.02.
*/
package javachip.repository.order;

import javachip.entity.order.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByProductSellerUserId(String userId);
    List<OrderItem> findByUserIdAndIsSubscriptionTrue(String userId);
}
