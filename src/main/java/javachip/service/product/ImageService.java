/*
파일명 : ProductServiceImpl.java
파일설명 : 상품 이미지 저장 로직을 처리하는 서비스 구현 클래스
작성자 : 정여진
기간 : 2025-05.03.
*/
package javachip.service.product;

import javachip.entity.product.Product;
import javachip.entity.product.ProductImage;
import javachip.repository.product.ProductImageRepository;
import javachip.repository.product.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

@Service
public class ImageService {

    // 이미지 테이블(ProductImage) 접근을 위한 레포지토리
    private final ProductImageRepository repository;

    // 상품 테이블(Product) 접근을 위한 레포지토리
    private final ProductRepository productRepository;

    public ImageService(ProductImageRepository repository, ProductRepository productRepository) {
        this.repository = repository;
        this.productRepository = productRepository;
    }

    /**
     * 상품 ID와 MultipartFile을 받아 이미지 데이터를 저장
     * 1. 상품 존재 여부 확인
     * 2. 이미지 파일을 Base64 문자열로 인코딩
     * 3. ProductImage 엔티티에 저장 및 DB에 저장
     *
     * @param productId 이미지와 연결할 상품 ID
     * @param file 업로드된 이미지 파일
     * @throws IOException 파일 변환 중 예외 발생 시
     */
    public void saveImage(Long productId, MultipartFile file) throws IOException {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));

        // 이미지 엔티티 생성 및 설정
        ProductImage img = new ProductImage();
        img.setProduct(product); // 연관관계로 연결
        img.setImageName(file.getOriginalFilename());
        byte[] bytes = file.getBytes();
        String base64Image = Base64.getEncoder().encodeToString(bytes); // ← 이걸 DB에 저장
        img.setImageData(base64Image);

        repository.save(img);
    }
}


