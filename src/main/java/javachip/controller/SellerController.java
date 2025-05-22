package javachip.controller;

import javachip.entity.Seller;
import javachip.service.SellerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/seller")
public class SellerController {
    private final SellerService sellerService;

    public SellerController(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    // ✅ 판매자 정보 조회
    @GetMapping("/profile")
    public ResponseEntity<Seller> getSeller(@RequestParam String userId) {
        Seller seller = sellerService.getSellerById(userId);
        return ResponseEntity.ok(seller);
    }

    // ✅ 판매자 정보 수정
    @PutMapping("/profile")
    public ResponseEntity<Seller> updateSeller(
            @RequestParam String userId,
            @RequestBody Seller updatedInfo) {
        Seller seller = sellerService.updateSeller(userId, updatedInfo);
        return ResponseEntity.ok(seller);
    }
}
