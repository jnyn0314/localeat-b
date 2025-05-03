package javachip.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConsumerSignUpRequest {

    private String userId;
    private String password;
    private String name;
    private String phone;
    private String email;
    private String address;
    private String birth;
    private String role;  // CONSUMER (String으로 받음)
    private String local; // 지역명 (예: 서울/경기/인천)
}