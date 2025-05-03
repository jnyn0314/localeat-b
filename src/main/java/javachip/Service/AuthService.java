package javachip.Service;

import javachip.DTO.LoginRequest;
import javachip.DTO.LoginResponse;
import javachip.DTO.SignUpRequest;
import javachip.entity.Consumer;
import javachip.entity.UserRole;
import javachip.repository.ConsumerRepository;
import javachip.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final ConsumerRepository consumerRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public AuthService(ConsumerRepository consumerRepository, PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.consumerRepository = consumerRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public void registerConsumer(SignUpRequest request) {
        Consumer consumer = new Consumer();
        consumer.setUserId(request.getUserId());
        consumer.setPassword(passwordEncoder.encode(request.getPassword()));
        consumer.setName(request.getName());
        consumer.setPhone(request.getPhone());
        consumer.setEmail(request.getEmail());
        consumer.setAddress(request.getAddress());
        consumer.setLocal(request.getLocal());
        consumer.setBirth(request.getBirth());
        consumer.setRole(UserRole.CONSUMER);

        consumerRepository.save(consumer);
    }

    public LoginResponse login(LoginRequest request) {
        Consumer user = consumerRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("해당 사용자가 존재하지 않습니다."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        return new LoginResponse(user.getUserId(), user.getName());
    }

    public boolean isUserIdDuplicate(String userId) {
        return userRepository.existsByUserId(userId);
    }

}


