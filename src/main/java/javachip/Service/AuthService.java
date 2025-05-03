package javachip.Service;

import javachip.DTO.LoginRequest;
import javachip.DTO.LoginResponse;
import javachip.DTO.SignUpRequest;
import javachip.entity.Consumer;
import javachip.entity.UserRole;
import javachip.repository.ConsumerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final ConsumerRepository consumerRepository;
    private final PasswordEncoder passwordEncoder;

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
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        return new LoginResponse(user.getUserId(), user.getName());
    }
}

