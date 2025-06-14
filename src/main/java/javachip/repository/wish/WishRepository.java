/*
파일명 : WishRepository.java
파일설명 : Wish 엔티티에 대한 데이터베이스 접근을 처리하는 JPA 레포지토리 인터페이스입니다.
작성자 : 정여진
작성일 : 2025-05-17.
*/
package javachip.repository.wish;

import javachip.entity.product.Product;
import javachip.entity.user.User;
import javachip.entity.wish.Wish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishRepository extends JpaRepository<Wish, Long> {
    // 사용자와 상품 기준으로 찜 존재 여부 조회
    Optional<Wish> findByUserAndProduct(User user, Product product);

    // 특정 사용자의 찜 목록 전체 조회
    List<Wish> findAllByUser(User user);

    // 사용자와 상품 기준으로 찜 삭제
    void deleteByUserAndProduct(User user, Product product);
}