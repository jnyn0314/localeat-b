package javachip.DTO;

public class LoginResponse {
    private String userId;
    private String password;

    public LoginResponse() {}

    public LoginResponse(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }
}
