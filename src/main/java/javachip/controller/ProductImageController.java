package javachip.controller;

import javachip.service.ImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class ProductImageController {
    private final ImageService imageService;

    public ProductImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/{productId}/images")
    public ResponseEntity<String> uploadImage(@PathVariable Long productId,
                                              @RequestParam("file") MultipartFile file) {
        try {
            imageService.saveImage(productId, file);
            return ResponseEntity.ok("이미지 업로드 성공");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("이미지 업로드 실패: " + e.getMessage());
        }
    }

    /*
    @GetMapping("/test-insert")
    public ResponseEntity<String> testInsert() {
        try {
            imageService.testInsertImageFromPath(1L, "D:\\study_2025\\4-1\\swsystemdevelop\\localeat-code\\localeat-f\\src\\pages\\productDetail.tomato.png");
            return ResponseEntity.ok("강제 이미지 삽입 성공!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("삽입 실패: " + e.getMessage());
        }
    }
    */
}
