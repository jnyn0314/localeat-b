package javachip.service;

import javachip.entity.Product;
import javachip.entity.ProductImage;
import javachip.repository.ProductImageRepository;
import javachip.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Base64;

@Service
public class ImageService {

    private final ProductImageRepository repository;

    private final ProductRepository productRepository;

    public ImageService(ProductImageRepository repository, ProductRepository productRepository) {
        this.repository = repository;
        this.productRepository = productRepository;
    }

    public void saveImage(Long productId, MultipartFile file) throws IOException {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));

        ProductImage img = new ProductImage();
        img.setProduct(product); // 연관관계로 연결
        img.setImageName(file.getOriginalFilename());
        byte[] bytes = file.getBytes();
        String base64Image = Base64.getEncoder().encodeToString(bytes); // ← 이걸 DB에 저장
        img.setImageData(base64Image);

        repository.save(img);
    }
}


