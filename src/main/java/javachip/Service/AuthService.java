package javachip.Service;

import javachip.DTO.LoginRequest;
import javachip.DTO.LoginResponse;
import javachip.DTO.SignUpRequest;
import javachip.entity.Consumer;
import javachip.entity.Seller;
import javachip.entity.UserRole;
import javachip.repository.ConsumerRepository;
import javachip.repository.SellerRepository;
import javachip.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final ConsumerRepository consumerRepository;
    private final SellerRepository sellerRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public AuthService(ConsumerRepository consumerRepository, SellerRepository sellerRepository, PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.consumerRepository = consumerRepository;
        this.sellerRepository = sellerRepository;
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

    public void registerSeller(SignUpRequest request) {
        Seller seller = new Seller();
        seller.setUserId(request.getUserId());
        seller.setPassword(passwordEncoder.encode(request.getPassword()));
        seller.setName(request.getName());
        seller.setPhone(request.getPhone());
        seller.setEmail(request.getEmail());
        seller.setAddress(request.getAddress());
        seller.setLocal(request.getLocal());
        seller.setBusiness_id(request.getBusiness_id());
        seller.setRole(UserRole.SELLER);

        sellerRepository.save(seller);
    }

    public LoginResponse login(LoginRequest request) {
        Consumer user = consumerRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("해당 사용자가 존재하지 않습니다."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        return new LoginResponse(user.getUserId(), user.getPassword());
    }

    public boolean isUserIdDuplicate(String userId) {
        return userRepository.existsByUserId(userId);
    }

}


