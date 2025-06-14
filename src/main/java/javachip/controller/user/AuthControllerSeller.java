package javachip.controller.user;

import javachip.dto.user.SignUpRequest;
import javachip.service.user.AuthServiceSeller;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/signUp/seller")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthControllerSeller {

    private final AuthServiceSeller authServiceSeller;

    public AuthControllerSeller(AuthServiceSeller authServiceSeller) {

        this.authServiceSeller = authServiceSeller;
    }

    @GetMapping("/check-id")
    public ResponseEntity<Boolean> checkUserIdDuplicate(@RequestParam String userId) {
        boolean isDuplicate = authServiceSeller.isUserIdDuplicate(userId);
        return ResponseEntity.ok(isDuplicate);
    }

    @PostMapping("/form")
    public ResponseEntity<String> signup(@RequestBody SignUpRequest request) {
        try {
            authServiceSeller.registerSeller(request);
            return ResponseEntity.ok("회원가입 성공");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("회원가입 실패: " + e.getMessage());
        }
    }
}


