package javachip.Service;

import javachip.entity.Consumer;
import javachip.repository.ConsumerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConsumerService {

    private final ConsumerRepository consumerRepository;

    public Consumer registerConsumer(Consumer consumer) {
        if (consumerRepository.existsById(consumer.getUserId())) {
            throw new IllegalArgumentException("이미 등록된 사용자입니다.");
        }
        return consumerRepository.save(consumer);
    }

    public Optional<Consumer> getConsumerByUserId(String userId) {
        return consumerRepository.findByUserId(userId);
    }

    public List<Consumer> getConsumersByBirth(String birth) {
        return consumerRepository.findByBirth(birth);
    }

    // Cart 연동 예정 시
    /*
    public Optional<Consumer> getConsumerByCartId(Long cartId) {
        return consumerRepository.findByCart_CartId(cartId);
    }
    */
}
