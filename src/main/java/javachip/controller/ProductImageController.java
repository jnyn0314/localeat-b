package javachip.controller;

import javachip.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductImageController {

    private final ImageService imageService;

    @PostMapping("/images/{productId}")
    public ResponseEntity<String> uploadImage(@PathVariable Long productId,
                                              @RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("파일이 비어 있습니다.");
        }

        try {
            imageService.saveImage(productId, file);
            return ResponseEntity.ok("이미지 업로드 성공");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("이미지 업로드 실패: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("알 수 없는 오류 발생: " + e.getMessage());
        }
    }
}
