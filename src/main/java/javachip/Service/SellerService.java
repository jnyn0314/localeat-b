package javachip.Service;

import javachip.entity.Seller;
import javachip.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SellerService {

    private final SellerRepository sellerRepository;

    public Seller registerSeller(Seller seller) {
        if (sellerRepository.existsById(seller.getUserId())) {
            throw new IllegalArgumentException("이미 등록된 사업자입니다.");
        }
        return sellerRepository.save(seller);
    }

    public Optional<Seller> getSellerByUserId(String userId) {
        return sellerRepository.findByUserId(userId);
    }

    public Optional<Seller> getSellerByBusinessId(String businessId) {
        return sellerRepository.findByBusinessId(businessId);
    }
}
