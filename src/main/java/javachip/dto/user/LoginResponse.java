package javachip.dto.user;

import javachip.entity.UserRole;
import javachip.entity.LocalType;

public class LoginResponse {
    private String userId;
    private String password;
    private UserRole role;
    private LocalType local;

    public LoginResponse() {}

    public LoginResponse(String userId, String password, UserRole role, LocalType local) {
        this.userId = userId;
        this.password = password;
        this.role = role;
        this.local = local;
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public UserRole getRole() { return role; }

    public LocalType getLocal() { return local; }
}
