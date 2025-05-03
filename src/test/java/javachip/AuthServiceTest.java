package javachip;

import jakarta.transaction.Transactional;
import javachip.DTO.SignUpRequest;
import javachip.Service.AuthService;
import javachip.entity.Consumer;
import javachip.repository.ConsumerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // 실제 DB를 사용하는 경우
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private ConsumerRepository consumerRepository;

    @Test
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
        assertThat(consumer.getBirth()).isEqualTo("1990-01-01");
        assertThat(consumer.getPassword()).isNotEqualTo("password123"); // 암호화 확인
    }
}
