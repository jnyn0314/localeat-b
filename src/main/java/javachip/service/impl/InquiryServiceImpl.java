package javachip.service.impl;

import javachip.dto.inquiry.InquiryRequest;
import javachip.dto.inquiry.InquiryResponse;
import javachip.entity.cart.Consumer;
import javachip.entity.inquiry.Inquiry;
import javachip.entity.product.Product;
import javachip.repository.inquiry.InquiryRepository;
import javachip.repository.product.ProductRepository;
import javachip.repository.user.ConsumerRepository;
import javachip.service.alarm.AlarmService;
import javachip.service.inquiry.InquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Transactional
public class InquiryServiceImpl implements InquiryService {

    private final InquiryRepository inquiryRepository;
    private final ProductRepository productRepository;
    private final ConsumerRepository consumerRepository;
    private final AlarmService alarmService;

    @Override
    public InquiryResponse createInquiry(InquiryRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));

        Consumer consumer = consumerRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));

        Inquiry inquiry = request.toEntity(product, consumer);
        Inquiry saved = inquiryRepository.save(inquiry);

        // 판매자에게 문의 등록 알림 전송
        alarmService.notifySellerOnInquiry(saved);

        return InquiryResponse.fromEntity(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InquiryResponse> getInquiriesByProduct(Long productId) {
        return inquiryRepository.findByProductIdOrderByCreatedAtDesc(productId).stream()
                .map(InquiryResponse::fromEntity)
                .collect(toList());
    }

    @Override
    public InquiryResponse addAnswer(Long inquiryId, String sellerId, String answer) {
        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new IllegalArgumentException("문의가 존재하지 않습니다."));

        String ownerId = inquiry.getProduct().getSeller().getUserId();
        if (!ownerId.equals(sellerId)) {
            throw new SecurityException("본인 상품의 문의만 답변할 수 있습니다.");
        }

        inquiry.setAnswer(answer);
        inquiry.setAnsweredAt(java.time.LocalDateTime.now());

        // 구매자에게 답변 등록 알림 전송
        alarmService.notifyBuyerOnInquiryAnswer(inquiry);

        return InquiryResponse.fromEntity(inquiry);
    }

    @Override
    public void deleteInquiry(Long inquiryId, String userId) {
        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new IllegalArgumentException("문의가 존재하지 않습니다."));

        if (!inquiry.getConsumer().getUserId().equals(userId)) {
            throw new SecurityException("본인이 작성한 문의만 삭제할 수 있습니다.");
        }

        if (inquiry.getAnswer() != null) {
            throw new IllegalStateException("답변이 달린 문의는 삭제할 수 없습니다.");
        }

        inquiryRepository.delete(inquiry);
    }


}
