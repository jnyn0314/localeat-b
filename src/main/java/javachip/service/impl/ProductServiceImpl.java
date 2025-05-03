package javachip.service.impl;

import javachip.dto.ProductDto;
import javachip.entity.Product;
import javachip.repository.ProductRepository;
import javachip.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;

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
        Product saved = repository.save(dto.toEntity());
        return ProductDto.fromEntity(saved);
    }

    @Override
    public ProductDto updateProduct(Long id, ProductDto dto) {
        Product existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Product updated = dto.toEntity();
        updated.setId(existing.getId());

        return ProductDto.fromEntity(repository.save(updated));
    }

    @Override
    public void deleteProduct(Long id) {
        repository.deleteById(id);
    }
}
