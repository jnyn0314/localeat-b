/*
파일명 : ProductController.java
파일설명 : 상품 조회, 등록, 수정, 삭제 및 폼 옵션 제공
작성자 : 정여진
기간 : 2025-05-03
*/
package javachip.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import javachip.dto.product.ProductDto;
import javachip.entity.LocalType;
import javachip.repository.ProductRepository;
import javachip.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult; // 꼭 있어야 해!


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final Logger log = LoggerFactory.getLogger(ProductController.class);
    private final ProductRepository productRepository;
    private final ProductService productService;

    /**
     * 상품 단건 조회 (이미지 포함)
     * @param id 상품 ID
     * @return ProductDto (존재하지 않으면 404 Not Found)
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProduct(
            @PathVariable Long id,
            @RequestParam(required = false) String userId
    ) {
        ProductDto dto = (userId == null)
                ? productService.getProductById(id)
                : productService.getProductById(id, userId);
        return ResponseEntity.ok(dto);
    }

    /**
     * 상품 등록
     * - Validation 검증 포함
     * - 오류 시 필드별 에러 메시지 반환
     * @param dto 상품 데이터
     * @param bindingResult 유효성 검증 결과
     * @return 저장된 상품 정보 또는 에러 메시지
     */
    @PostMapping
    public ResponseEntity<?> createProduct(@Valid @RequestBody ProductDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(Map.of("errors", errors));
        }

        ProductDto saved = productService.saveProduct(dto);
        return ResponseEntity.ok(saved);
    }


    /**
     * 모든 상품 리스트 조회
     * @return 상품 리스트
     */
    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<ProductDto> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    /**
     * 상품 삭제
     * - 이미지 등 연관 엔티티도 함께 삭제됨
     * @param id 상품 ID
     */
    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        log.info("DELETE 요청 받음 - deleteProduct(), id = {}", id);
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * 상품 수정
     * @param id 수정 대상 상품 ID
     * @param dto 수정할 상품 정보
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody ProductDto dto) {
        // 서비스에 위임해서 해당 id의 상품을 수정한다.
        productService.updateProduct(id, dto);
        return ResponseEntity.ok("상품 수정 완료");
    }

    /**
     * 상품 등록/수정 폼을 위한 옵션 값 제공
     * - 지역(LocalType), 공동구매 옵션, 등급 옵션
     * @return Enum 값을 문자열로 구성한 맵
     */
    @GetMapping("/form-options")
    public ResponseEntity<Map<String, List<String>>> getFormOptions() {
        Map<String, List<String>> result = new HashMap<>();
        result.put("localTypes", Arrays.stream(LocalType.values()).map(Enum::name).toList());
        result.put("groupBuyOptions", List.of("O", "X"));
        result.put("gradeBOptions", List.of("O", "X"));
        return ResponseEntity.ok(result);
    }

    @GetMapping("/latest")
    public ResponseEntity<List<ProductDto>> getLatestProducts() {
        return ResponseEntity.ok(productService.getLatestProducts());
    }

    // 검색과 필터
    @GetMapping("/search/filter")
    public ResponseEntity<List<ProductDto>> searchProductsWithFilter(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String tag
    ) {
        List<ProductDto> result = productService.searchProducts(keyword, tag);
        return ResponseEntity.ok(result);
    }
}
