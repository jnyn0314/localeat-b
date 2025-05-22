package javachip.controller;

import javachip.entity.Consumer;
import javachip.service.ConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/consumer")
public class ConsumerController {
    @Autowired
    private ConsumerService consumerService;

    @GetMapping("/profile")
    public ResponseEntity<Consumer> getProfile(@RequestParam String userId) {
        return ResponseEntity.ok(consumerService.getConsumerById(userId));
    }

    @PutMapping("/profile")
    public ResponseEntity<Consumer> updateProfile(
            @RequestParam String userId,
            @RequestBody Consumer consumerInfo) {
        return ResponseEntity.ok(consumerService.updateConsumer(userId, consumerInfo));
    }
}
