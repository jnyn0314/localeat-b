import javachip.dto.GroupBuyCreateRequest;
import javachip.dto.GroupBuyCreateResponse;
import javachip.entity.Consumer;
import javachip.entity.GroupBuy;
import javachip.entity.Product;
import javachip.repository.ConsumerRepository;
import javachip.repository.GroupBuyRepository;
import javachip.repository.ProductRepository;
import javachip.service.GroupBuyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GroupBuyServiceTest {

    @InjectMocks
    private GroupBuyService groupBuyService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private GroupBuyRepository groupBuyRepository;

    @Mock
    private ConsumerRepository consumerRepository;

    @Test
    void 공동구매_생성_정상동작() {
        // given
        String userId = "consumer123";
        Long productId = 100L;

        GroupBuyCreateRequest request = GroupBuyCreateRequest.builder()
                .productId(productId)
                .description("친구들과 함께 저렴하게 구매해요")
                .quantity(2)
                .deadline(LocalDate.now().plusDays(2))
                .build();

        Consumer mockConsumer = new Consumer();
        mockConsumer.setUserId(userId);

        Product mockProduct = Product.builder()
                .id(productId)
                .product_name("사과")
                .is_group_buy(GroupBuyOption.YES)
                .local(LocalType.SEOUL)
                .max_participants(5)
                .build();

        when(consumerRepository.findById(userId)).thenReturn(Optional.of(mockConsumer));
        when(productRepository.findById(productId)).thenReturn(Optional.of(mockProduct));
        when(groupBuyRepository.save(any(GroupBuy.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        GroupBuyCreateResponse response = groupBuyService.createGroupBuy(request, userId);

        // then
        assertNotNull(response);
        assertEquals(productId, response.getProductId());
        assertEquals("친구들과 함께 저렴하게 구매해요", response.getDescription());
        assertEquals(GroupBuyStatus.IN_PROGRESS, response.getStatus());
        assertEquals(1, response.getCurrentParticipants());
        assertEquals(LocalType.SEOUL, response.getLocal());

        // verify interactions
        verify(consumerRepository).findById(userId);
        verify(productRepository).findById(productId);
        verify(groupBuyRepository).save(any(GroupBuy.class));
    }
}
