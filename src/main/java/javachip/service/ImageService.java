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
        ProductImage img = new ProductImage();
        img.setId(productId); // ID 생성 방식 자유
        img.setProduct_id(productId);
        img.setImage_name(file.getOriginalFilename());
        img.setImage_data(file.getBytes());
        repository.save(img);
    }

    public void testInsertImageFromPath(Long productId, String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException("파일이 존재하지 않습니다: " + filePath);
        }

        ProductImage img = new ProductImage();
        img.setProduct_id(productId);
        img.setImage_name(file.getName());

        FileInputStream fis = new FileInputStream(file);
        img.setImage_data(fis.readAllBytes());

        repository.save(img);
    }
}


