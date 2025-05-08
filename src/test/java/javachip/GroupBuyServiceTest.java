package javachip;

import javachip.dto.GroupBuyCreateRequest;
import javachip.dto.GroupBuyCreateResponse;
import javachip.entity.*;
import javachip.repository.ConsumerRepository;
import javachip.repository.GroupBuyRepository;
import javachip.repository.ProductRepository;
import javachip.service.GroupBuyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional // 테스트 끝나면 롤백됨
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
        Long productId;

        // 실제 Consumer 저장
        Consumer consumer = new Consumer();
        consumer.setUserId(userId);
        consumerRepository.save(consumer);

        // 실제 Product 저장
        Product product = Product.builder()
                .productName("고구마")
                .isGroupBuy(true)
                .local(LocalType.GYEONGGI)
                .maxParticipants(4)
                .build();
        product = productRepository.save(product);
        productId = product.getId();

        GroupBuyCreateRequest request = GroupBuyCreateRequest.builder()
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
        assertTrue(saved.isPresent());
        assertEquals(GroupBuyStatus.RECRUITING, saved.get().getStatus());
        assertEquals(1, saved.get().getParticipants().size());
    }
}