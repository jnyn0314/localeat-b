package javachip.service;

import javachip.entity.Seller;
import javachip.repository.SellerRepository;
import org.springframework.stereotype.Service;

@Service
public class SellerService {
    private final SellerRepository sellerRepository;

    public SellerService(SellerRepository sellerRepository) {
        this.sellerRepository = sellerRepository;
    }

    public Seller getSellerById(String userId) {
        return sellerRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 판매자가 존재하지 않습니다: " + userId));
    }

    public Seller updateSeller(String userId, Seller updatedInfo) {
        Seller seller = getSellerById(userId);

        seller.setName(updatedInfo.getName());
        seller.setEmail(updatedInfo.getEmail());
        seller.setAddress(
                updatedInfo.getAddress() != null ? updatedInfo.getAddress() : "주소 미입력"
        );
        seller.setLocal(updatedInfo.getLocal());
        seller.setBusinessId(updatedInfo.getBusinessId());

        return sellerRepository.save(seller);
    }
}
