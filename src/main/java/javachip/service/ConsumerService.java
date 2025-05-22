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

    public Consumer updateConsumer(String userId, Consumer updatedInfo) {
        Consumer consumer = consumerRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자 없음"));

        consumer.setName(updatedInfo.getName());
        consumer.setPhone(updatedInfo.getPhone());
        consumer.setEmail(updatedInfo.getEmail());
        consumer.setAddress(updatedInfo.getAddress());
        consumer.setLocal(updatedInfo.getLocal());

        // ✅ 비밀번호가 입력된 경우에만 업데이트
        if (updatedInfo.getPassword() != null && !updatedInfo.getPassword().isBlank()) {
            consumer.setPassword(updatedInfo.getPassword()); // 🔐 보안상 bcrypt 암호화 필요
        }

        return consumerRepository.save(consumer);
    }

}
