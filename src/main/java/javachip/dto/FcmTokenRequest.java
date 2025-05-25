package javachip.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FcmTokenRequest {
    private String userId;
    private String fcmToken;
}
