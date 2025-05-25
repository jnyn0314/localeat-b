package javachip.controller;

import javachip.dto.FcmTokenRequest;
import javachip.entity.FcmToken;
import javachip.repository.FcmTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/fcm")
@RequiredArgsConstructor
public class FcmController {

    private final FcmTokenRepository tokenRepository;

    @PostMapping("/token")
    public ResponseEntity<Void> saveToken(@RequestBody FcmTokenRequest request) {
        FcmToken token = new FcmToken();
        token.setUserId(request.getUserId());
        token.setToken(request.getFcmToken());
        tokenRepository.save(token);
        return ResponseEntity.ok().build();
    }
}

