package javachip.controller;

import javachip.entity.Seller;
import javachip.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/sellers")
@RequiredArgsConstructor
public class SellerController {

    private final SellerService sellerService;

    @PostMapping("/register")
    public ResponseEntity<Seller> register(@RequestBody Seller seller) {
        Seller registered = sellerService.registerSeller(seller);
        return ResponseEntity.ok(registered);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Seller> getByUserId(@PathVariable String userId) {
        return sellerService.getSellerByUserId(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-business-id")
    public ResponseEntity<Seller> getByBusinessId(@RequestParam String businessId) {
        return sellerService.getSellerByBusinessId(businessId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
