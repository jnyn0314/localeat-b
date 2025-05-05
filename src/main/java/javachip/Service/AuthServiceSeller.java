package javachip.service;

import javachip.dto.LoginRequest;
import javachip.dto.LoginResponse;
import javachip.dto.SignUpRequest;
import javachip.entity.Seller;
import javachip.entity.UserRole;
import javachip.repository.SellerRepository;
import javachip.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service("AuthServiceSeller")
public class AuthServiceSeller implements AuthService {

    private final SellerRepository sellerRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public AuthServiceSeller(SellerRepository sellerRepository, PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.sellerRepository = sellerRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
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

    @Override
    public LoginResponse login(LoginRequest request) {
        Seller user = sellerRepository.findById(request.getUserId())
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


