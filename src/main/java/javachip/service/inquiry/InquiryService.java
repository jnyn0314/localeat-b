package javachip.service.inquiry;

import javachip.dto.inquiry.InquiryRequest;
import javachip.dto.inquiry.InquiryResponse;

import java.util.List;

public interface InquiryService {

    InquiryResponse createInquiry(InquiryRequest request);

    List<InquiryResponse> getInquiriesByProduct(Long productId);

    InquiryResponse addAnswer(Long inquiryId, String sellerId, String answer);

    void deleteInquiry(Long inquiryId, String userId);
}
