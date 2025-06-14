package javachip.controller.product;

import javachip.entity.product.Product;
import javachip.entity.product.ProductImage;
import javachip.repository.product.ProductImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {

    private final ProductImageRepository imageRepository;

    // ✅ 이미지 업로드 (Base64로 저장)
    @PostMapping("/{productId}")
    public ResponseEntity<String> uploadImage(
            @PathVariable Long productId,
            @RequestParam("file") MultipartFile file) {

        try {
            byte[] bytes = file.getBytes();
            String base64Image = Base64.getEncoder().encodeToString(bytes);
            String contentType = file.getContentType(); // 예: "image/jpeg"

            ProductImage image = new ProductImage();
            Product product = new Product();
            product.setId(productId);
            image.setProduct(product);
            image.setImageData(base64Image);
            image.setImageType(contentType);

            imageRepository.save(image);
            return ResponseEntity.ok("이미지 업로드 성공");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("이미지 업로드 실패");
        }
    }

    // ✅ 이미지 조회 (Base64 → byte[] 변환 후 반환)
    @GetMapping("/{productId}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long productId) {
        List<ProductImage> imageList = imageRepository.findByProduct_Id(productId);
        if (!imageList.isEmpty()) {
            ProductImage image = imageList.get(0);
            byte[] decodedImage = Base64.getDecoder().decode(image.getImageData());

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(image.getImageType()))
                    .body(decodedImage);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/by-product/{productId}")
    public ResponseEntity<byte[]> getImageByProductId(@PathVariable Long productId) {
        List<ProductImage> imageList = imageRepository.findByProduct_Id(productId);
        if (!imageList.isEmpty()) {
            ProductImage image = imageList.get(0);
            byte[] decodedImage = Base64.getDecoder().decode(image.getImageData());

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(image.getImageType()))
                    .body(decodedImage);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
