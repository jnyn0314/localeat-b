/*
파일명 : ProductController.java
파일설명 : ProductController
작성자 : 정여진
기간 : 2025-05-03
*/
package javachip.controller;

import javachip.dto.ProductDto;
import javachip.entity.Product;
import javachip.repository.ProductRepository;
import javachip.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javachip.entity.LocalType;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

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
}