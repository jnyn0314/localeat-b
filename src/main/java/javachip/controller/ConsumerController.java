package javachip.controller;

import javachip.DTO.ConsumerSignUpRequest;
import javachip.entity.Consumer;
import javachip.Service.ConsumerService;
import javachip.entity.LocalType;
import javachip.entity.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/consumers")
@RequiredArgsConstructor
public class ConsumerController {

    private final ConsumerService consumerService;

    public ResponseEntity<Consumer> register(@RequestBody ConsumerSignUpRequest dto) {
        // Enum 파싱 및 엔티티 변환
        Consumer consumer = Consumer.builder()
                .userId(dto.getUserId())
                .password(dto.getPassword()) // 해싱은 서비스에서 처리
                .name(dto.getName())
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .address(dto.getAddress())
                .birth(dto.getBirth())
                .role(UserRole.valueOf(dto.getRole()))
                .local(LocalType.valueOf(dto.getLocal()))
                .build();

        Consumer registered = consumerService.registerConsumer(consumer);
        return ResponseEntity.ok(registered);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Consumer> getByUserId(@PathVariable String userId) {
        return consumerService.getConsumerByUserId(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-birth")
    public ResponseEntity<List<Consumer>> getByBirth(@RequestParam String birth) {
        return ResponseEntity.ok(consumerService.getConsumersByBirth(birth));
    }
}
