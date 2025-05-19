/*
파일명 : ProductServiceImpl.java
파일설명 : ProductServiceImpl
작성자 : 정여진
기간 : 2025-05.03.
*/
package javachip.service;

import javachip.dto.product.ProductDto;
import java.util.List;

public interface ProductService {
    List<ProductDto> getAllProducts();
    ProductDto getProductById(Long id);
    ProductDto saveProduct(ProductDto dto);
    void updateProduct(Long id, ProductDto dto);
    void deleteProduct(Long id);
    List<ProductDto> getLatestProducts();
    List<ProductDto> searchProducts(String keyword, String tag);
}
