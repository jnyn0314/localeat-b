package javachip.DTO;

public class LoginResponse {
    private String userId;
    private String password;

    public LoginResponse(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }
}
