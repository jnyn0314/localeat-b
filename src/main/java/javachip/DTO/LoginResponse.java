package javachip.DTO;

public class LoginResponse {
    private String userId;
    private String name;

    public LoginResponse(String userId, String name) {
        this.userId = userId;
        this.name = name;
    }
}
