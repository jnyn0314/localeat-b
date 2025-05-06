/*
파일명 : ProductImageRepository.java
파일설명 : ProductImageRepository 레포지토리
작성자 : 정여진
기간 : 2025-05.03.
*/
package javachip.repository;

import javachip.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    List<ProductImage> findByProductId(Long productId);
    void deleteAllByProductId(Long productId);
}
