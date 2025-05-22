package javachip.service;

import javachip.entity.Consumer;
import javachip.repository.ConsumerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConsumerService {
    @Autowired
    private ConsumerRepository consumerRepository;

    public Consumer getConsumerById(String userId) {
        return consumerRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 소비자를 찾을 수 없습니다."));
    }

    public Consumer updateConsumer(String userId, Consumer updateInfo) {
        Consumer consumer = getConsumerById(userId);

        consumer.setName(updateInfo.getName());
        consumer.setEmail(updateInfo.getEmail());
        consumer.setPhone(updateInfo.getPhone());
        consumer.setAddress(updateInfo.getAddress());
        consumer.setLocal(updateInfo.getLocal());
        consumer.setPassword(updateInfo.getPassword()); // 암호화 고려

        return consumerRepository.save(consumer);
    }
}
