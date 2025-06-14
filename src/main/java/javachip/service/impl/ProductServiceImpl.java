/*
파일명 : ProductServiceImpl.java
파일설명 : 상품 등록, 조회, 수정, 삭제를 담당하는 서비스 구현 클래스
작성자 : 정여진
기간 : 2025-05.03.
*/

package javachip.service.impl;

import jakarta.transaction.Transactional;
import javachip.dto.product.ProductDto;
import javachip.entity.product.GradeBOption;
import javachip.entity.product.LocalType;
import javachip.entity.product.Product;
import javachip.entity.user.Seller;
import javachip.entity.user.User;
import javachip.entity.wish.Wish;
import javachip.repository.product.ProductImageRepository;
import javachip.repository.product.ProductRepository;
import javachip.repository.user.UserRepository;
import javachip.repository.wish.WishRepository;
import javachip.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;
    private final ProductImageRepository productImageRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final WishRepository wishRepository;

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
        return ProductDto.fromEntity(product); // 여긴 찜 정보 없음
    }

    /**
     * 상품 id와 userId로 상품 조회
     * 찜 정보를 가져오기 위함.
     * */
    @Override
    public ProductDto getProductById(Long id, String userId) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

        boolean isWished = false;
        Long wishId = null;

        if (userId != null) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));
            Optional<Wish> wishOpt = wishRepository.findByUserAndProduct(user, product);
            if (wishOpt.isPresent()) {
                isWished = true;
                wishId = wishOpt.get().getId();
            }
        }

        return ProductDto.fromEntity(product, isWished, wishId);
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
    public Map<String, Object> getLatestProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        var pageResult = repository.findAll(pageable);
        
        List<ProductDto> products = pageResult.getContent().stream()
                .map(ProductDto::fromEntity)
                .toList();

        return Map.of(
            "content", products,
            "totalElements", pageResult.getTotalElements(),
            "totalPages", pageResult.getTotalPages(),
            "currentPage", pageResult.getNumber(),
            "size", pageResult.getSize()
        );
    }

    @Override
    public List<ProductDto> searchProducts(String keyword, String tag) {
        List<Product> products = List.of();

        if (tag != null) {
            // 1. tag가 등급 코드인지 시도
            try {
                GradeBOption grade = GradeBOption.valueOf(tag); // tag = "A" or "B"
                if (keyword != null && !keyword.isBlank()) {
                    products = productRepository.findByProductNameContainingIgnoreCaseAndProductGrade(keyword, grade);
                } else {
                    products = productRepository.findByProductGrade(grade);
                }
                return products.stream()
                        .map(ProductDto::fromEntity)
                        .toList();
            } catch (IllegalArgumentException ignored) {
                // 등급이 아님 → 지역 코드로 간주
            }

            // 2. 공동구매 필터
            if (tag.equalsIgnoreCase("GROUP_BUY")) {
                if (keyword != null && !keyword.isBlank()) {
                    products = productRepository.findByProductNameContainingIgnoreCaseAndIsGroupBuy(keyword, true);
                } else {
                    products = productRepository.findByIsGroupBuy(true);
                }
                return products.stream().map(ProductDto::fromEntity).toList();
            }

            // 2. tag가 지역 코드인지 시도
            try {
                LocalType localType = LocalType.valueOf(tag);
                if (keyword != null && !keyword.isBlank()) {
                    products = productRepository.findByProductNameContainingIgnoreCaseAndLocal(keyword, localType);
                } else {
                    products = productRepository.findByLocal(localType);
                }
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("유효하지 않은 tag입니다 (지역 또는 등급): " + tag);
            }

        } else if (keyword != null && !keyword.isBlank()) {
            // 태그 없고 키워드만 있을 때
            products = productRepository.findByProductNameContainingIgnoreCase(keyword);
        }

        return products.stream()
                .map(ProductDto::fromEntity)
                .toList();
    }

    @Override
    public List<ProductDto> getProductsBySeller(String sellerId) {
        return repository.findBySeller_UserId(sellerId)
            .stream()
            .map(ProductDto::fromEntity)
            .toList();
    }

}
