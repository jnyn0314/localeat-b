/*
파일명 : OrdersRepository.java
파일설명 : OrdersRepository 레포지토리
작성자 : 정여진
기간 : 2025-05.02.
*/
package javachip.repository;

import javachip.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrdersRepository extends JpaRepository<Orders, Long> {
    List<Orders> findByUserId(String userId);
}