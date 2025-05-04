package javachip.controller;

import javachip.DTO.LoginRequest;
import javachip.DTO.LoginResponse;
import javachip.Service.AuthService;
import javachip.Service.AuthServiceFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class LoginController {
    private final AuthServiceFactory authServiceFactory;

    public LoginController(AuthServiceFactory authServiceFactory) {
        this.authServiceFactory = authServiceFactory;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        AuthService service = authServiceFactory.getService(request.getUserRole());
        LoginResponse response = service.login(request);
        return ResponseEntity.ok(response);
    }
}
