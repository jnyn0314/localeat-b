/*
파일명 : ProductServiceImpl.java
파일설명 : ProductServiceImpl
작성자 : 정여진
기간 : 2025-05.03.
*/

package javachip.service.impl;

import jakarta.transaction.Transactional;
import javachip.dto.ProductDto;
import javachip.entity.Product;
import javachip.entity.Seller;
import javachip.entity.User;
import javachip.repository.ProductImageRepository;
import javachip.repository.ProductRepository;
import javachip.repository.UserRepository;
import javachip.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.plaf.SliderUI;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;
    private final ProductImageRepository productImageRepository;
    private final UserRepository userRepository;

    // 공동구매, 카트 등 나중에 생기면 얘네도 delete할때 먼저 지워야 함.

    @Override
    public List<ProductDto> getAllProducts() {
        return repository.findAll().stream()
                .map(ProductDto::fromEntity)
                .toList();
    }

    @Override
    public ProductDto getProductById(Long id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return ProductDto.fromEntity(product);
    }

    @Override
    public ProductDto saveProduct(ProductDto dto) {
        User user = userRepository.findById(dto.getSellerId())
                .orElseThrow(() -> new RuntimeException("판매자 정보를 찾을 수 없습니다."));
        Seller seller = (Seller) user;

        Product saved = repository.save(dto.toEntity(seller));
        return ProductDto.fromEntity(saved);
    }

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

    @Transactional
    public void deleteProduct(Long id) {
        Optional<Product> product = repository.findById(id);
        if (product.isPresent()) {
            System.out.println(">> 삭제 대상 존재, product = " + product.get());
            // 자식 엔티티 먼저 삭제
            productImageRepository.deleteAllByProductId(id);

            // 부모 엔티티 삭제
            repository.deleteById(id);
        } else {
            System.out.println(">> 삭제 대상 없음, id = " + id);
        }
    }
}
