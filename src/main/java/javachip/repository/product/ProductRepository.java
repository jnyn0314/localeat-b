/*
파일명 : ProductRepository.java
파일설명 : 상품 등록 리포지토리(interface)
작성자 : 정여진
작성일 : 2025.05.13.
*/
package javachip.repository.product;

import javachip.entity.product.GradeBOption;
import javachip.entity.product.LocalType;
import javachip.entity.product.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // 키워드 검색
    List<Product> findByProductNameContainingIgnoreCase(String keyword);

    // 태그만으로 검색
    List<Product> findByLocal(LocalType local);

    // 태그 + 키워드 동시 검색
    List<Product> findByProductNameContainingIgnoreCaseAndLocal(String keyword, LocalType local);

    // 태그 + 공동구매 키워드
    List<Product> findByProductNameContainingIgnoreCaseAndProductGrade(String keyword, GradeBOption grade);

    // b급 상품 필터링
    List<Product> findByProductGrade(GradeBOption grade);

    // 공동구매
    List<Product> findByIsGroupBuy(boolean isGroupBuy);

    // 공동구매
    List<Product> findByProductNameContainingIgnoreCaseAndIsGroupBuy(String keyword, boolean isGroupBuy);

    List<Product> findBySeller_UserId(String sellerId);
}