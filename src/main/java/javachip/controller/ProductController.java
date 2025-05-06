/*
파일명 : ProductController.java
파일설명 : ProductController
작성자 : 정여진
기간 : 2025-05-03
*/
package javachip.controller;

import jakarta.transaction.Transactional;
import javachip.dto.ProductDto;
import javachip.entity.Product;
import javachip.repository.ProductRepository;
import javachip.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final Logger log = LoggerFactory.getLogger(ProductController.class);
    private final ProductRepository productRepository;
    private final ProductService productService;

    @GetMapping("/{id:[\\d]+}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto dto) {
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

}
