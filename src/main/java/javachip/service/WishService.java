/*
파일명 : WishService.java
파일설명 : 찜 기능의 서비스 계층 인터페이스입니다.
작성자 : 정여진
작성일 : 2025-05-18.
*/
package javachip.service;

import javachip.dto.product.ProductDto;

import java.util.List;

public interface WishService {
    // 찜 상태 토글 (찜 → 해제 또는 해제 → 찜)
    boolean toggleWish(String userId, Long productId); // true면 추가, false면 삭제

    // 사용자가 찜한 상품 목록 조회
    List<ProductDto> getWishedProducts(String userId);

    // 찜 지우기
    void deleteByUserAndProduct(String userId, Long productId);
}