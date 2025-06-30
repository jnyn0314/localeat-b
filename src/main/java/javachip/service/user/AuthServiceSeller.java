package javachip.service.user;

import javachip.dto.user.LoginRequest;
import javachip.dto.user.LoginResponse;
import javachip.dto.user.SignUpRequest;
import javachip.entity.product.LocalType;
import javachip.entity.user.Seller;
import javachip.entity.user.UserRole;
import javachip.repository.user.SellerRepository;
import javachip.repository.user.UserRepository;
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
        seller.setLocal(LocalType.fromDisplayName(request.getLocal()));
        seller.setBusinessId(request.getBusinessId());
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

        return new LoginResponse(user.getUserId(), user.getPassword(), user.getRole(), user.getLocal());
    }

    public boolean isUserIdDuplicate(String userId) {
        return userRepository.existsByUserId(userId);
    }

}


