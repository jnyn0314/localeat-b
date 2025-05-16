package javachip.service;

import javachip.dto.user.LoginRequest;
import javachip.dto.user.LoginResponse;

public interface  AuthService {
    LoginResponse login(LoginRequest request);
}
