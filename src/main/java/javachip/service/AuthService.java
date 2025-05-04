package javachip.service;

import javachip.dto.LoginRequest;
import javachip.dto.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);
}
