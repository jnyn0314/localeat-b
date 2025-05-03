package javachip.repository;

import javachip.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    //아래 코드 김소망이 추가함
    // 상품 이름 포함 검색 (대소문자 구분 없이)
    List<Product> findByProductNameContainingIgnoreCase(String productName);

    // 특정 판매자의 모든 상품 찾기 (Seller 객체 이용)

    // 특정 판매자 ID의 모든 상품 찾기 (Seller의 ID 이용)
    List<Product> findBySellerUserId(String sellerUserId);

    // 특정 지역(local) 상품 찾기 (Product에 local 필드가 있다고 가정)
    // List<Product> findByLocal(String local); // Product 엔티티의 local 필드 타입 및 이름 확인 필요

    // 구독 가능 상품 찾기
    List<Product> findByIsSubscriptionTrue();

    // 공동구매 가능 상품 찾기
    List<Product> findByIsGroupBuyTrue();
    //여기까지 김소망이 추가함
    
}