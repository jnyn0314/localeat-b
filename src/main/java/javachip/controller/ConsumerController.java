package javachip.controller;

import javachip.entity.Consumer;
import javachip.service.ConsumerService;
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

    @PostMapping("/register")
    public ResponseEntity<Consumer> register(@RequestBody Consumer consumer) {
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
