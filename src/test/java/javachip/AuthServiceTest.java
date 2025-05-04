package javachip;

import javachip.dto.SignUpRequest;
import javachip.service.AuthServiceConsumer;
import javachip.entity.Consumer;
import javachip.repository.ConsumerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AuthServiceTest {

    @Autowired
    private AuthServiceConsumer authService;

    @Autowired
    private ConsumerRepository consumerRepository;

    @Test
    @Transactional
    @Rollback(false)  // rollback을 막아야 실제로 DB에 저장됨
    void 회원가입_성공_테스트() {
        // given
        SignUpRequest request = new SignUpRequest();
        request.setUserId("testuser");
        request.setPassword("password123");
        request.setName("테스트 유저");
        request.setPhone("010-1234-5678");
        request.setEmail("test@example.com");
        request.setAddress("서울시 어딘가");
        request.setLocal("서울");
        request.setBirth("1990-01-01");

        // when
        authService.registerConsumer(request);

        // then
        Consumer consumer = consumerRepository.findById("testuser")
                .orElseThrow(() -> new RuntimeException("저장 실패"));

        assertThat(consumer.getName()).isEqualTo("테스트 유저");
    }
}
