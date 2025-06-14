package javachip.service.user;

import javachip.dto.user.LoginRequest;
import javachip.dto.user.LoginResponse;
import javachip.dto.user.SignUpRequest;
import javachip.entity.cart.Cart;
import javachip.entity.cart.Consumer;
import javachip.entity.product.LocalType;
import javachip.entity.user.UserRole;
import javachip.repository.user.ConsumerRepository;
import javachip.repository.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service("AuthServiceConsumer")
public class AuthServiceConsumer implements AuthService {

    private final ConsumerRepository consumerRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public AuthServiceConsumer(ConsumerRepository consumerRepository, PasswordEncoder passwordEncoder, UserRepository userRepository) {
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
        consumer.setLocal(LocalType.fromDisplayName(request.getLocal()));
        consumer.setBirth(request.getBirth());
        consumer.setRole(UserRole.CONSUMER);

        Cart cart = new Cart();
        consumer.setCart(cart);

        consumerRepository.save(consumer);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        Consumer user = consumerRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("해당 사용자가 존재하지 않습니다."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        return new LoginResponse(user.getUserId(), user.getPassword(), user.getRole(), user.getLocal());
    }

    public boolean isUserIdDuplicate(String userId) {
        return userRepository.existsByUserId(userId);
    }

}


