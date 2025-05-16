package javachip.controller;

import javachip.dto.user.LoginRequest;
import javachip.dto.user.LoginResponse;
import javachip.service.AuthService;
import javachip.service.AuthServiceFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
@CrossOrigin(origins = "http://localhost:3000")
public class LoginController {
    private final AuthServiceFactory authServiceFactory;

    public LoginController(AuthServiceFactory authServiceFactory) {
        this.authServiceFactory = authServiceFactory;
    }

    @PostMapping
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        AuthService service = authServiceFactory.getService(request.getUserRole());
        LoginResponse response = service.login(request);
        return ResponseEntity.ok(response);
    }
}
