package javachip.controller;

import javachip.dto.SignUpRequest;
import javachip.service.AuthServiceConsumer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/signUp/consumer")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    private final AuthServiceConsumer authService;

    public AuthController(AuthServiceConsumer authService) {
        this.authService = authService;
    }

    @GetMapping("/check-id")
    public ResponseEntity<Boolean> checkUserIdDuplicate(@RequestParam String userId) {
        boolean isDuplicate = authService.isUserIdDuplicate(userId);
        return ResponseEntity.ok(isDuplicate);
    }

    @PostMapping("/form")
    public ResponseEntity<String> signup(@RequestBody SignUpRequest request) {
        try {
            authService.registerConsumer(request);
            return ResponseEntity.ok("회원가입 성공");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("회원가입 실패: " + e.getMessage());
        }
    }
}


