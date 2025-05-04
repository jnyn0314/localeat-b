package javachip;

import javachip.DTO.SignUpRequest;
import javachip.Service.AuthServiceSeller;
import javachip.entity.Seller;
import javachip.repository.SellerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AuthServiceSellerTest {

    @Autowired
    private AuthServiceSeller authServiceSeller;

    @Autowired
    private SellerRepository sellerRepository;

    @Test
    @Transactional
    @Rollback(false)  // rollback을 막아야 실제로 DB에 저장됨
    void 회원가입_성공_테스트() {
        // given
        SignUpRequest request = new SignUpRequest();
        request.setUserId("asgvcx");
        request.setPassword("1234");
        request.setName("김소망");
        request.setPhone("010-1234-5678");
        request.setEmail("test@example.com");
        request.setAddress("인천 어딘가");
        request.setLocal("인천");
        request.setBusiness_id("1990-01-01");

        // when
        authServiceSeller.registerSeller(request);

        // then
        Seller seller = sellerRepository.findById("asgvcx")
                .orElseThrow(() -> new RuntimeException("저장 실패"));

        assertThat(seller.getName()).isEqualTo("김소망");
    }
}
