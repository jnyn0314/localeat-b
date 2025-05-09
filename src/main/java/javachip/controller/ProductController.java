/*
파일명 : ProductController.java
파일설명 : ProductController
작성자 : 정여진
기간 : 2025-05-03
*/
package javachip.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import javachip.dto.ProductDto;
import javachip.entity.LocalType;
import javachip.entity.Product;
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

    @GetMapping("/{id:[\\d]+}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        return productRepository.findWithImagesById(id)
                .map(product -> ResponseEntity.ok(ProductDto.fromEntity(product)))
                .orElse(ResponseEntity.notFound().build());
    }

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

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<ProductDto> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        log.info("DELETE 요청 받음 - deleteProduct(), id = {}", id);
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody ProductDto dto) {
        // 서비스에 위임해서 해당 id의 상품을 수정한다.
        productService.updateProduct(id, dto);
        return ResponseEntity.ok("상품 수정 완료");
    }

    @GetMapping("/form-options")
    public ResponseEntity<Map<String, List<String>>> getFormOptions() {
        Map<String, List<String>> result = new HashMap<>();
        result.put("localTypes", Arrays.stream(LocalType.values()).map(Enum::name).toList());
        result.put("groupBuyOptions", List.of("O", "X"));
        result.put("gradeBOptions", List.of("O", "X"));
        return ResponseEntity.ok(result);
    }

}
