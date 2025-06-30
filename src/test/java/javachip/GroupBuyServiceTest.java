package javachip;

import javachip.dto.groupbuy.GroupBuyCreateRequest;
import javachip.dto.groupbuy.GroupBuyCreateResponse;
import javachip.entity.cart.Consumer;
import javachip.entity.groupbuy.GroupBuy;
import javachip.entity.groupbuy.GroupBuyStatus;
import javachip.entity.product.LocalType;
import javachip.entity.product.Product;
import javachip.entity.user.UserRole;
import javachip.repository.user.ConsumerRepository;
import javachip.repository.groupbuy.GroupBuyRepository;
import javachip.repository.product.ProductRepository;
import javachip.service.groupbuy.GroupBuyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@SpringBootTest
@Transactional
class GroupBuyServiceTest {

    @Autowired
    private GroupBuyService groupBuyService;

    @Autowired
    private ConsumerRepository consumerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private GroupBuyRepository groupBuyRepository;

    @Test
    void 공동구매_생성_실제DB저장() {
        // given
        String userId = "testUser";
        String password = "password123"; // 비밀번호
        String name = "테스트 사용자"; // 이름 추가
        String phone = "010-1234-5678"; // 전화번호 추가
        String email = "testuser@example.com"; // 이메일 추가
        String address = "서울시 강남구 테헤란로 123"; // 주소 추가
        LocalType local = LocalType.GANGWON; // 지역 추가 (LocalType에 맞는 값을 설정)
        Long productId;

        // 실제 Consumer 저장
        Consumer consumer = new Consumer();
        consumer.setUserId(userId);
        consumer.setPassword(password); // 비밀번호 설정
        consumer.setName(name); // 이름 설정
        consumer.setPhone(phone); // 전화번호 설정
        consumer.setEmail(email); // 이메일 설정
        consumer.setAddress(address); // 주소 설정
        consumer.setLocal(local); // 지역 설정
        consumer.setRole(UserRole.CONSUMER);
        GroupBuyCreateRequest request = null;
        consumer.setBirth("1990-01-01");
        consumerRepository.save(consumer); // 저장 후 ID 확인

        // 실제 Product 저장
        Product product = Product.builder()
                .productName("고구마")
                .isGroupBuy(true)
                .local(LocalType.CHUNGCHEONG)
                .maxParticipants(4)
                .build();
        product = productRepository.save(product); // 저장 후 ID 확인
        productId = product.getId();

        request = GroupBuyCreateRequest.builder()
                .productId(productId)
                .description("맛있는 고구마를 같이 사요!")
                .quantity(3)
                .deadline(LocalDate.now().plusDays(3))
                .build();

        // when
        GroupBuyCreateResponse response = groupBuyService.createGroupBuy(request, userId);

        // then
        assertNotNull(response.getGroupBuyId());
        Optional<GroupBuy> saved = groupBuyRepository.findById(response.getGroupBuyId());
        assertTrue(saved.isPresent(), "GroupBuy should be saved in DB");

        // 확인: 값이 DB에 저장된 후 실제로 확인하기
        // 트랜잭션이 정상적으로 커밋되었는지 확인
        groupBuyRepository.flush();  // 명시적으로 flush() 호출

        assertEquals(GroupBuyStatus.RECRUITING, saved.get().getStatus());
        assertEquals(1, saved.get().getParticipants().size());
    }
}

