package javachip.controller;

import javachip.dto.user.ConsumerDto;
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
    public ResponseEntity<ConsumerDto> getConsumerProfile(@RequestParam String userId) {
        Consumer consumer = consumerService.getConsumerById(userId);

        ConsumerDto dto = new ConsumerDto(
                consumer.getUserId(),
                consumer.getName(),
                consumer.getPhone(),
                consumer.getEmail(),
                consumer.getAddress(),
                consumer.getLocal(),
                consumer.getBirth()
        );

        return ResponseEntity.ok(dto);
    }

    @PutMapping("/profile")
    public ResponseEntity<Consumer> updateProfile(
            @RequestParam String userId,
            @RequestBody Consumer consumerInfo) {
        return ResponseEntity.ok(consumerService.updateConsumer(userId, consumerInfo));
    }
}
