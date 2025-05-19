/*
파일명 : ProductServiceImpl.java
파일설명 : 상품 등록, 조회, 수정, 삭제를 담당하는 서비스 구현 클래스
작성자 : 정여진
기간 : 2025-05.03.
*/

package javachip.service.impl;

import jakarta.transaction.Transactional;
import javachip.dto.product.ProductDto;
import javachip.entity.LocalType;
import javachip.entity.Product;
import javachip.entity.Seller;
import javachip.entity.User;
import javachip.repository.ProductImageRepository;
import javachip.repository.ProductRepository;
import javachip.repository.UserRepository;
import javachip.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.swing.plaf.SliderUI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;
    private final ProductImageRepository productImageRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    // 공동구매, 카트 등 나중에 생기면 얘네도 delete할때 먼저 지워야 함.

    /**
     * 모든 상품을 조회하여 ProductDto 리스트로 반환
     */
    @Override
    public List<ProductDto> getAllProducts() {
        return repository.findAll().stream()
                .map(ProductDto::fromEntity)
                .toList();
    }

    /**
     * 상품 ID로 상품 단건 조회
     * @param id 상품 ID
     * @return ProductDto
     */
    @Override
    public ProductDto getProductById(Long id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return ProductDto.fromEntity(product);
    }

    /**
     * 상품 등록 (DTO → Entity 변환 → 저장)
     * @param dto ProductDto
     * @return 저장된 ProductDto
     */
    @Override
    public ProductDto saveProduct(ProductDto dto) {
        User user = userRepository.findById(dto.getSellerId())
                .orElseThrow(() -> new RuntimeException("판매자 정보를 찾을 수 없습니다."));
        Seller seller = (Seller) user;

        Product saved = repository.save(dto.toEntity(seller));
        return ProductDto.fromEntity(saved);
    }

    /**
     * 상품 수정
     * 기존 상품 조회 후, DTO → Entity 변환하여 ID 유지한 채 갱신
     */
    @Override
    public void updateProduct(Long id, ProductDto dto) {
        Product existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        User user = userRepository.findById(dto.getSellerId())
                .orElseThrow(() -> new RuntimeException("판매자 정보를 찾을 수 없습니다."));

        Seller seller = (Seller) user; // 다운캐스팅 해줘야 함.
        Product updated = dto.toEntity(seller);
        updated.setId(existing.getId());

        ProductDto.fromEntity(repository.save(updated));
    }

    /**
     * 상품 삭제
     * - 이미지 등 자식 엔티티 먼저 삭제
     * - 이후 상품 삭제
     * @param id 상품 ID
     */
    @Transactional
    public void deleteProduct(Long id) {
        Optional<Product> product = repository.findById(id);
        if (product.isPresent()) {
            System.out.println("삭제 대상 존재, product = " + product.get());
            // 자식 엔티티 먼저 삭제
            productImageRepository.deleteAllByProductId(id);

            // 부모 엔티티 삭제
            repository.deleteById(id);
        } else {
            System.out.println("삭제 대상 없음, id = " + id);
        }
    }

    @Override
    public List<ProductDto> getLatestProducts() {
        Pageable get8 = PageRequest.of(0, 8, Sort.by("createdAt").descending());
        return repository.findAll(get8).stream()
                .map(ProductDto::fromEntity)
                .toList();
    }

    @Override
    public List<ProductDto> searchProducts(String keyword, String tag) {
        List<Product> products;

        LocalType localType = null;
        if (tag != null) {
            try {
                localType = LocalType.valueOf(tag);
                System.out.println("💡 전달받은 tag = " + tag);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("유효하지 않은 지역 코드입니다: " + tag);
            }
        }

        if (localType != null && (keyword == null || keyword.isBlank())) {
            products = productRepository.findByLocal(localType);
        } else if (localType != null && keyword != null && !keyword.isBlank()) {
            products = productRepository.findByProductNameContainingIgnoreCaseAndLocal(keyword, localType);
        } else if (keyword != null && !keyword.isBlank()) {
            products = productRepository.findByProductNameContainingIgnoreCase(keyword);
        } else {
            products = List.of(); // or 전체 리스트
        }

        return products.stream()
                .map(ProductDto::fromEntity)
                .collect(Collectors.toList());
    }

}
