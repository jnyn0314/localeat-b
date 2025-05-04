package javachip.Service;

import javachip.DTO.LoginRequest;
import javachip.DTO.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);
}
