package javachip.dto.user;

import javachip.entity.UserRole;

public class LoginResponse {
    private String userId;
    private String password;
    private UserRole role;

    public LoginResponse() {}

    public LoginResponse(String userId, String password, UserRole role) {
        this.userId = userId;
        this.password = password;
        this.role = role;
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public UserRole getRole() { return role; }
}
