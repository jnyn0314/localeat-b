package javachip.service;

import javachip.entity.ProductImage;
import javachip.repository.ProductImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@Service
public class ImageService {

    private final ProductImageRepository repository;

    public ImageService(ProductImageRepository repository) {
        this.repository = repository;
    }

    public void saveImage(Long productId, MultipartFile file) throws IOException {
        try {
            if (file.isEmpty()) {
                throw new IllegalArgumentException("파일이 비어 있습니다.");
            }

            ProductImage image = new ProductImage();
            image.setProductId(productId);
            image.setImage_name(file.getOriginalFilename());
            image.setImage_data(file.getBytes());

            System.out.println("▶ 저장 시도: " + image.getImage_name() + ", 크기=" + file.getSize());

            repository.save(image);
            System.out.println("저장 성공!");
        } catch (Exception e) {
            System.out.println("이미지 저장 실패: " + e.getMessage());
            throw e;
        }
    }

    public void testInsertImageFromPath(Long productId, String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException("파일이 존재하지 않습니다: " + filePath);
        }

        ProductImage img = new ProductImage();
        img.setProductId(productId);
        img.setImage_name(file.getName());

        FileInputStream fis = new FileInputStream(file);
        img.setImage_data(fis.readAllBytes());

        repository.save(img);
    }
}


