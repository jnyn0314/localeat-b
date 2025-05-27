package javachip.service.impl;

import javachip.dto.inquiry.InquiryRequest;
import javachip.dto.inquiry.InquiryResponse;
import javachip.entity.*;
import javachip.repository.*;
import javachip.service.InquiryService;
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

    @Override
    public InquiryResponse createInquiry(InquiryRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));

        Consumer consumer = consumerRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));

        Inquiry inquiry = request.toEntity(product, consumer);
        Inquiry saved = inquiryRepository.save(inquiry);
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
