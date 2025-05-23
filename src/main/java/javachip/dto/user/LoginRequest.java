package javachip.dto.user;

import javachip.entity.LocalType;
import javachip.entity.UserRole;

public class LoginRequest {
    private String userId;
    private String password;
    private UserRole userRole;
    private LocalType local;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public LocalType getLocal() { return local; }
    public void setLocal(LocalType local) { this.local = local; }
}
